package com.kusitms.forpet.controller;

import com.kusitms.forpet.config.AppProperties;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.domain.UserRefreshToken;
import com.kusitms.forpet.dto.ApiResponse;
import com.kusitms.forpet.repository.UserRefreshTokenRepository;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.util.CookieUtils;
import com.kusitms.forpet.util.HeaderUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kusitms.forpet.domain.User;

import java.util.Date;

import static com.kusitms.forpet.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final AppProperties appProperties;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    // access token 재발급
    @GetMapping("/refresh")
    public ApiResponse refreshToken (HttpServletRequest request, HttpServletResponse response) {
        // access token 확인
        String accessToken = HeaderUtil.getAccessToken(request);
        if(!tokenProvider.validateToken(accessToken)) {
            return ApiResponse.invalidAccessToken();
        }

        // refresh token 확인
        String refreshToken = CookieUtils.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));
        if(!tokenProvider.validateToken(refreshToken)) {
            return ApiResponse.fail();
        }

        // userId로 DB refresh token 확인
        Long userId = tokenProvider.getUserIdFromToken(accessToken);
        User user = new User();
        user.setUserId(userId);
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(user, refreshToken);
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
}
