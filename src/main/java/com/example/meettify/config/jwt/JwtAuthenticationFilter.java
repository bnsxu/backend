package com.example.meettify.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.channels.AcceptPendingException;
import java.nio.file.AccessDeniedException;

@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER = "Authorization";
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = request;
        String token = resolveToken(httpServletRequest);

        if(token != null && jwtProvider.validateToken(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTH_HEADER);
        log.info("token: {}", token);

        if(token.startsWith("Bearer ") && StringUtils.hasText(token)) {
            return token.substring(7);
        } else {
            return null;
        }
    }
}
