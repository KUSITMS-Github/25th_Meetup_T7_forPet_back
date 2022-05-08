package com.kusitms.forpet.security.oauth2;

import com.kusitms.forpet.config.AppProperties;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.domain.UserRefreshToken;
import com.kusitms.forpet.exception.BadRequestException;
import com.kusitms.forpet.repository.UserRefreshTokenRepository;
import com.kusitms.forpet.security.TokenProvider;
import com.kusitms.forpet.security.UserPrincipal;
import com.kusitms.forpet.service.UserService;
import com.kusitms.forpet.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.kusitms.forpet.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.kusitms.forpet.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REFRESH_TOKEN;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;

    private final AppProperties appProperties;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private final UserService userService;

    private final UserRefreshTokenRepository userRefreshTokenRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        logger.debug("토큰 발행 완료");
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userService.findByEmail(userPrincipal.getEmail());

        // 회원가입 여부를 판단하여 다르게 리다이렉트
        if (user.getNickname() == null) {
            return UriComponentsBuilder.fromUriString("http://localhost:8080/member/join/terms")
                    .queryParam("id", user.getUserId())
                    .build().toUriString();
        }
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication" + redirectUri.get());
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        //refresh 토큰 설정
        String refreshToken = tokenProvider.createRefreshToken(authentication);
        // refresh 토큰 DB 저장
        // userId로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(user);
        if(userRefreshToken != null) {
            // DB에 refresh token 업데이트
            userRefreshToken.setRefreshToken(refreshToken);
        } else {
            // 없는 경우 새로 등록
            userRefreshToken = new UserRefreshToken();
            userRefreshToken.setUserId(user);
            userRefreshToken.setRefreshToken(refreshToken);
            userRefreshTokenRepository.saveAndFlush(userRefreshToken);
        }

        int cookieMaxAge = (int) appProperties.getAuth().getRefreshTokenExpiry() / 60;

        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken, cookieMaxAge);

        String accessToken = tokenProvider.createAccessToken(authentication);

        logger.debug("refresh token : " + refreshToken);
        System.out.println("access_token : " + UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", accessToken)
                .build().toUriString());

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", accessToken)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}
