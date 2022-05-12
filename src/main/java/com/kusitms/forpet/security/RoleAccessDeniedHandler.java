package com.kusitms.forpet.security;

import org.apache.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RoleAccessDeniedHandler implements AccessDeniedHandler {
    private final String MESSAGE_FORBIDDEN_USER = "동물 등록 카드를 인증한 사용자만 이용 가능합니다.";
    private final String MESSAGE_FORBIDDEN_GUEST = "로그인이 필요합니다.";

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if(accessDeniedException instanceof AccessDeniedException) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication != null &&
                    ((UserPrincipal) authentication.getPrincipal()).getAuthorities().contains(new SimpleGrantedAuthority(Role.USER.getValue()))) {
                // USER 권한 : 동물 등록 카드를 인증하지 않은 사용자
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        MESSAGE_FORBIDDEN_USER);
            } else if(authentication != null &&
                    ((UserPrincipal) authentication.getPrincipal()).getAuthorities().contains(new SimpleGrantedAuthority(Role.GUEST.getValue()))) {
                // GUEST 권한 : 회원가입이 되어 있지 않은 사용자
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        MESSAGE_FORBIDDEN_GUEST);
            }
        }
    }
}
