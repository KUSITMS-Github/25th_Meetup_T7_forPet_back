package com.kusitms.forpet.controller;

import com.kusitms.forpet.config.AppProperties;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.domain.UserRefreshToken;
import com.kusitms.forpet.dto.response.ApiResponse;
import com.kusitms.forpet.dto.response.ErrorCode;
import com.kusitms.forpet.dto.LoginDto;
import com.kusitms.forpet.exception.CustomException;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.service.JWTTokenService;
import com.kusitms.forpet.service.UserService;
import com.kusitms.forpet.util.CookieUtils;
import com.kusitms.forpet.util.HeaderUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final TokenProvider tokenProvider;
    private final JWTTokenService jwtTokenService;
    private final UserService userService;
    private final AppProperties appProperties;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    /*
     리다이렉트된 id를 받아와 회원가입 여부를 반환
     */
    @GetMapping("/auth/signup")
    public ApiResponse isSignUp(@RequestParam(value="id") Long id,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        boolean isSignUp = true;
        String accessToken;

        //id로 회원가입 여부 확인
        User user = userService.findByUserId(id);
        if(StringUtils.isEmpty(user.getNickname())) {
            // 회원가입이 되어 있지 않다면 access token 발급
            isSignUp = false;
            accessToken = jwtTokenService.createJWTToken(user);
        } else {
            // 회원가입이 되어 있다면 access token 발급과 refresh token 쿠키 저장
            accessToken = jwtTokenService.createJWTToken(user, request, response);
        }

        return ApiResponse.success("data", new LoginDto(isSignUp, accessToken));
    }

    // access token 재발급
    @GetMapping("/auth/refresh")
    public ApiResponse refreshToken (HttpServletRequest request, HttpServletResponse response) {
        // access token 확인, 유효성 검사
        String accessToken = HeaderUtil.getAccessToken(request);
        tokenExceptionHandler(accessToken);

        // refresh token 확인, 유효성 검사
        String refreshToken = CookieUtils.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));

        boolean isAccessTokenExpired = tokenExceptionHandler(accessToken);
        boolean isRefreshTokenExpired = tokenExceptionHandler(refreshToken);

        // jwt가 만료 기간을 넘지 않았다면 재발급 과정이 필요 없음.
        if(isAccessTokenExpired && isRefreshTokenExpired) {
            throw new CustomException(ErrorCode.NOT_EXPIRED_TOKEN);
        }

        // userId로 DB refresh token 확인
        Long userId = tokenProvider.getUserIdFromExpiredToken(accessToken);
        User user = User.builder()
                        .userId(userId).build();

        // access 토큰 갱신
        String newAccessToken = tokenProvider.createAccessToken(userId);

        UserRefreshToken userRefreshToken = userService.findByUserIdAndRefreshToken(user, refreshToken);

        long validTime = tokenProvider.getValidTime(refreshToken);
        // refresh token 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
        if(validTime <= THREE_DAYS_MSEC) {
            // refresh token 설정
            refreshToken = tokenProvider.createRefreshToken(userId);

            userRefreshToken.setRefreshToken(refreshToken);
            userService.save(userRefreshToken);

            int cookieMaxAge = (int) appProperties.getAuth().getRefreshTokenExpiry() / 60;

            CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken, cookieMaxAge);
        }
        return ApiResponse.success("token", newAccessToken);
    }

    @GetMapping("/logout")
    public ApiResponse logout(HttpServletRequest request) {
        // access token 확인
        String accessToken = HeaderUtil.getAccessToken(request);

        // 토큰 유효성 검사
        tokenExceptionHandler(accessToken);

        // access token으로 userId 가져옴
        Long userId = tokenProvider.getUserIdFromToken(accessToken);
        // db에 refresh token 삭제
        userService.deleteRefreshTokenByUserId(userId);

        return ApiResponse.success("message", "로그아웃 되었습니다.");
    }

    // 토큰 validation 검사
    public boolean tokenExceptionHandler(String token) {
        try {
            tokenProvider.validateToken(token);
        } catch (SecurityException | MalformedJwtException e) {
            new CustomException(ErrorCode.INVALID_AUTH_TOKEN);
        } catch (ExpiredJwtException e) {
            // 만료된 토큰에 대해서는 에러 응답 보내지 않음.
            return false;
        } catch (UnsupportedJwtException e) {
            new CustomException(ErrorCode.UNSUPPORTED_AUTH_TOKEN);
        } catch (IllegalArgumentException e) {
            new CustomException(ErrorCode.WRONG_TOKEN);
        } catch (Exception e) {
            new CustomException(ErrorCode.UNKNOWN_ERROR);
        }

        return true;
    }
}
