package com.example.meettify.repository.jwt;

import com.example.meettify.entity.jwt.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    TokenEntity findByEmail(String email);
}
