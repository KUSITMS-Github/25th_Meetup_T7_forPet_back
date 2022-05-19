package com.kusitms.forpet.security;

import com.kusitms.forpet.dto.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {
        String exception = (String)request.getAttribute("exception");

        if(exception == null) {
            setResponse(response, ErrorCode.UNKNOWN_ERROR);
        }
        //잘못된 타입의 토큰인 경우
        else if(exception.equals(ErrorCode.INVALID_AUTH_TOKEN.toString())) {
            setResponse(response, ErrorCode.INVALID_AUTH_TOKEN);
        }
        //토큰 만료된 경우
        else if(exception.equals(ErrorCode.EXPIRED_AUTH_TOKEN.toString())) {
            setResponse(response, ErrorCode.EXPIRED_AUTH_TOKEN);
        }
        //지원되지 않는 토큰인 경우
        else if(exception.equals(ErrorCode.UNSUPPORTED_AUTH_TOKEN.toString())) {
            setResponse(response, ErrorCode.UNSUPPORTED_AUTH_TOKEN);
        }
        else {
            setResponse(response, ErrorCode.WRONG_TOKEN);
        }

    }

    //한글 출력을 위해 getWriter() 사용
    private void setResponse(HttpServletResponse response, ErrorCode exceptionCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("timestamp", LocalDateTime.now());
        responseJson.put("status", exceptionCode.getHttpStatus().value());
        responseJson.put("error", exceptionCode.getHttpStatus().name());
        responseJson.put("message", exceptionCode.getDetail());
        responseJson.put("code", exceptionCode);

        response.getWriter().print(responseJson);
    }

}
