package com.example.meettify.config.security;

import com.example.meettify.config.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/*
*   worker  : 유요한
*   work    : 시큐리티 설정들을 넣고 빈으로 등록할 수 있는 클래스
*   date    : 2024/09/19
* */


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
// 시큐리티에서 메서드 수준의 보안 설정을 활성화
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
    private final JwtProvider jwtProvider;

    // 이렇게 처리하면 확작성과 유연성이 늘어난다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }
}
