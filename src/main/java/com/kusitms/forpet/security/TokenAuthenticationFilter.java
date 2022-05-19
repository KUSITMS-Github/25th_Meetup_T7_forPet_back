package com.kusitms.forpet.security;

import com.kusitms.forpet.config.AppProperties;
import com.kusitms.forpet.domain.User;
import com.kusitms.forpet.domain.UserRefreshToken;
import com.kusitms.forpet.dto.ApiResponse;
import com.kusitms.forpet.dto.ErrorCode;
import com.kusitms.forpet.repository.UserRefreshTokenRepository;
import com.kusitms.forpet.service.UserService;
import com.kusitms.forpet.util.CookieUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private AppProperties appProperties;
    @Autowired
    private UserRefreshTokenRepository userRefreshTokenRepository;


    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);
        try {
            System.out.println("access token in header : " + jwt);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromToken(jwt);

                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", ErrorCode.INVALID_AUTH_TOKEN.toString());
        } catch (ExpiredJwtException e) {
            log.error(ErrorCode.EXPIRED_AUTH_TOKEN.toString());
            request.setAttribute("exception", ErrorCode.EXPIRED_AUTH_TOKEN.toString());
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", ErrorCode.UNSUPPORTED_AUTH_TOKEN.toString());
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", ErrorCode.WRONG_TOKEN.toString());
        } catch (Exception e) {
            log.error("================================================");
            log.error("JwtFilter - doFilterInternal() 오류발생");
            log.error("token : {}", jwt);
            log.error("Exception Message : {}", e.getMessage());
            log.error("Exception StackTrace : {");
            e.printStackTrace();
            log.error("}");
            log.error("================================================");
            request.setAttribute("exception", ErrorCode.UNKNOWN_ERROR.toString());
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /*
    // 토큰 재발급
    private String refreshToken(HttpServletRequest request, HttpServletResponse response, String expiredToken) {
        String refreshToken = CookieUtils.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));
        // refresh token 검증
        if(tokenProvider.validateToken(refreshToken)) {
            // userId로 DB의 refresh Token이 있는지 확인
            Long userId = tokenProvider.getUserIdFromExpiredToken(expiredToken);
            User user = User.builder()
                    .userId(userId).build();
            System.out.println("userID : " + userId + ", refresh token : " + refreshToken);
            UserRefreshToken userRefreshToken = userService.findByUserIdAndRefreshToken(user, refreshToken);
            if(userRefreshToken == null) {
                return "denied";
            }

            // 새로운 토큰 발급
            String newToken = tokenProvider.createAccessToken(userId);

            // refresh token 기간이 3일 이하로 남은 경우에 refresh token 갱신
            long validTime = tokenProvider.getValidTime(refreshToken);
            if(validTime <= THREE_DAYS_MSEC) {
                // refresh token 설정
                refreshToken = tokenProvider.createRefreshToken(userId);

                userRefreshToken.setRefreshToken(refreshToken);
                userRefreshTokenRepository.save(userRefreshToken);

                int cookieMaxAge = (int) appProperties.getAuth().getRefreshTokenExpiry() / 60;

                CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
                CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken, cookieMaxAge);
            }
            return newToken;
        }
        return "denied";
    }
    */

}
