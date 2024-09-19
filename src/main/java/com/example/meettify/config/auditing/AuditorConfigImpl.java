package com.example.meettify.config.auditing;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
/*
*   worker  : 유요한
*   work    : Auditing 기능을 사용하기 위해서 인증받은 이메일을 가져온다.
*   date    : 2024/09/19
* */


public class AuditorConfigImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        // 인증받은 이메일을 가져온다.
        // 인증받지 않으면 어나니머스 유저로 나온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = "";
        if (authentication != null) {
            email = authentication.getName();
        }
        return Optional.of(email);
    }
}
