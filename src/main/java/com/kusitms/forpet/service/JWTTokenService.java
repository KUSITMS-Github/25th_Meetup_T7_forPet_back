package com.kusitms.forpet.service;

import com.kusitms.forpet.config.AppProperties;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.domain.UserRefreshToken;
import com.kusitms.forpet.repository.UserRefreshTokenRep;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.kusitms.forpet.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REFRESH_TOKEN;


@Service
@RequiredArgsConstructor
public class JWTTokenService {
    private final AppProperties appProperties;
    private final UserRefreshTokenRep userRefreshTokenRepository;
    private final TokenProvider tokenProvider;

    /*
    회원가입, 카카오 로그인에서 사용되는 JWT 발급
     */
    public String createJWTToken(User user,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        // access token 발급
        String accessToken = tokenProvider.createAccessToken(user.getUserId());
        // refresh token 발급
        String refreshToken = tokenProvider.createRefreshToken(user.getUserId());

        // refresh 토큰 DB 저장
        UserRefreshToken userRefreshToken = new UserRefreshToken();
        userRefreshToken.setUserId(user);
        userRefreshToken.setRefreshToken(refreshToken);
        userRefreshTokenRepository.saveAndFlush(userRefreshToken);

        int cookieMaxAge = (int) appProperties.getAuth().getRefreshTokenExpiry() / 60;

        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken, cookieMaxAge);

        return accessToken;
    }

    /**
     *  회원 가입 진행시 필요한 JWT 발급
      */
    public String createJWTToken(User user) {
        // access token 발급
        String accessToken = tokenProvider.createAccessToken(user.getUserId());

        return accessToken;
    }

}
