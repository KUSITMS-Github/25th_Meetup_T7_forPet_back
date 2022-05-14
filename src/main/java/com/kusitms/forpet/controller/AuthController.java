package com.kusitms.forpet.controller;

import com.kusitms.forpet.config.AppProperties;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.domain.UserRefreshToken;
import com.kusitms.forpet.dto.ApiResponse;
import com.kusitms.forpet.dto.LoginDto;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.service.UserService;
import com.kusitms.forpet.util.CookieUtils;
import com.kusitms.forpet.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
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
    private final UserService userService;
    private final AppProperties appProperties;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    /*
    oauth2에서 redirect 되는 uri에 token이 함께 온다.
     */
    @GetMapping("/oauth2/redirect")
    public ApiResponse login(@RequestParam(value="token") String token) {
        Long userId = tokenProvider.getUserIdFromToken(token);
        User user = userService.findByUserId(userId);

        boolean isSignup = true;
        // 회원 가입이 필요한 경우
        if(user.getName() == null) {
            isSignup = false;
        }

        return ApiResponse.success("data", new LoginDto(token, isSignup));
    }

    // access token 재발급
    @GetMapping("/auth/refresh")
    public ApiResponse refreshToken (HttpServletRequest request, HttpServletResponse response) {
        // access token 확인, 유효성 검사
        String accessToken = HeaderUtil.getAccessToken(request);
        if(!tokenProvider.validateToken(accessToken)) {
            return ApiResponse.invalidAccessToken();
        }

        // refresh token 확인, 유효성 검사
        String refreshToken = CookieUtils.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));
        if(!tokenProvider.validateToken(refreshToken)) {
            return ApiResponse.invalidRefreshToken();
        }

        // jwt가 만료 기간을 넘지 않았다면 재발급 과정이 필요 없음.
        if(tokenProvider.isExpiredToken(accessToken) && tokenProvider.isExpiredToken(refreshToken)) {
            return ApiResponse.notExpiredTokenYet();
        }

        // userId로 DB refresh token 확인
        Long userId = tokenProvider.getUserIdFromExpiredToken(accessToken);
        User user = User.builder()
                        .userId(userId).build();
        UserRefreshToken userRefreshToken = userService.findByUserIdAndRefreshToken(user, refreshToken);
        if(userRefreshToken == null) {
            return ApiResponse.invalidRefreshToken();
        }

        String newAccessToken = tokenProvider.createAccessToken(userId);

        long validTime = tokenProvider.getValidTime(refreshToken);
        // refresh token 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
        if(validTime <= THREE_DAYS_MSEC) {
            // refresh token 설정
            refreshToken = tokenProvider.createRefreshToken(userId);

            userRefreshToken.setRefreshToken(refreshToken);

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
        if(!tokenProvider.validateToken(accessToken)) {
            return ApiResponse.invalidAccessToken();
        }

        // access token으로 userId 가져옴
        Long userId = tokenProvider.getUserIdFromToken(accessToken);
        // db에 refresh token 삭제
        userService.deleteRefreshTokenByUserId(userId);

        return ApiResponse.success("message", "로그아웃 되었습니다.");
    }
}
