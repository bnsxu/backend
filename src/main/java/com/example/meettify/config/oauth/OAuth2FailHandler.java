package com.example.meettify.config.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 *   worker : 유요한
 *   work : 소셜 로그인을 실패하면 바디에 JSON으로 보내준다.
 *   date : 2024/09/22
 * */
@Log4j2
@RestControllerAdvice
@Component
@RequiredArgsConstructor
public class OAuth2FailHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // 실패 시 수행할 로직을 구현
        log.error("OAuth2 로그인 실패: {}", exception.getMessage());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error 발생 : " , exception.getMessage());

        // JSON 응답 전송
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
