package com.example.meettify.config.auditing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
// JPA의 Auditing 기능을 사용해서 등독자, 수정자, 등록 시간, 업데이트 시간을 기록
@EnableJpaAuditing
public class AuditingConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        // AuditorConfigImpl에서 가져온 사용자 id를 사용한다.
        return new AuditorConfigImpl();
    }
}
