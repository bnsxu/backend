package com.example.meettify.config.security;

import com.example.meettify.config.jwt.JwtProvider;
import com.example.meettify.config.jwt.JwtSecurityConfig;
import com.example.meettify.config.oauth.OAuth2FailHandler;
import com.example.meettify.config.oauth.OAuth2SuccessHandler;
import com.example.meettify.config.oauth.PrincipalOAuthUserService;
import com.example.meettify.exception.jwt.JwtAccessDeniedHandler;
import com.example.meettify.exception.jwt.JwtAuthenticationEntryPoint;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailHandler oAuth2FailHandler;
    private final PrincipalOAuthUserService principalOAuthUserService;

    // 이렇게 처리하면 확작성과 유연성이 늘어난다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())  // Disable CSRF as you're using JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable());

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.PUT, "/api/v1/members/")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/members/{memberId}")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/members/.*")
                        .permitAll()

                        .requestMatchers(HttpMethod.POST,"/api/v1/notice/")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/notice/{noticeId}")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/notice/{noticeId}")
                        .hasRole("ADMIN")
                        .requestMatchers("/api/v1/notice/*")
                        .permitAll()

                        .requestMatchers("/api/v1/items/*")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/items/")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/items/{itemId}")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/items/{itemId}")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/api/v1/cart/{cartId}")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/cart/")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/cart/{cartId}")
                        .hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/swagger-resources/.*")
                        .permitAll()
                        .requestMatchers("/swagger-ui/.*")
                        .permitAll()
                        .requestMatchers("/actuator/.*")
                        .permitAll()
                );


        // JWT Configuration
        http.apply(new JwtSecurityConfig(jwtProvider));

// Exception handling for authentication/authorization issues
        http
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        .accessDeniedHandler(new JwtAccessDeniedHandler())
                );

// OAuth2 Login configuration
        http
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(principalOAuthUserService)
                        )
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailHandler)
                );

// Enable actuator access without authentication
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                );


        return http.build();
    }


    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customize() {
        return p -> {
            p.setOneIndexedParameters(true);    // 1-based pagination
            p.setMaxPageSize(10);               // Maximum 10 items per page
        };
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }


}
