package com.example.meettify.config.jwt;

import com.example.meettify.dto.jwt.TokenDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
/*
 *   writer  : 유요한
 *   work    : 로그인 시 프론트에게 JWT를 생성해주고 검증해주는 클래스
 *   date    : 2024/09/19
 * */

@Component
@Log4j2
public class JwtProvider {
    private static final String AUTHORITIES_KEY = "auth";
    @Value("${jwt.access.expiration}")
    private long accessTokenTime;
    @Value("${jwt.refresh.expiration}")
    private long refreshTokenTime;
    private Key key;

    // jwt secret key를 암호화
    public JwtProvider(@Value("${jwt.secret_key}") String secretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 일반 로그인시 토큰 발급
    public TokenDTO createToken(String email,
                                List<GrantedAuthority> authorities,
                                Long memberId) {
        // 클레임(Claims)에 유저 권한 등록
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_KEY, authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        claims.put("sub", email);

        Date now = new Date(System.currentTimeMillis());
        Date accessTokenExpire = new Date(now.getTime() + this.accessTokenTime);
        String accessToken = Jwts.builder()
                .setIssuedAt(now)
                .setClaims(claims)
                // 내용 exp : 토큰 만료 시간, 시간은 NumericDate 형식(예: 1480849143370)으로 하며
                // 항상 현재 시간 이후로 설정합니다.
                .setExpiration(accessTokenExpire)
                // 서명 : 비밀값과 함께 해시값을 ES256 방식으로 암호화
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Date refreshTokenExpire = new Date(now.getTime() + this.refreshTokenTime);
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(refreshTokenExpire)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        TokenDTO responseToken = TokenDTO.builder()
                .grantType("Bearer ")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberEmail(email)
                .memberId(memberId)
                .build();
        log.info("responseToken: {}", responseToken);
        return responseToken;
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내오는 코드
    // 토큰으로 클레임을 만들고 이를 이용해 유저 객체를 만들어서 최종적으로 authentication 객체를 리턴
    public Authentication getAuthentication(String token) {
        // 토큰 복호화 메서드
        Claims claims = parseClaims(token);

        if(claims.get(AUTHORITIES_KEY) == null) {
            throw new AccessDeniedException("권한 정보가 없는 토큰입니다.");
        }

        // 권한 정보 가져오기
        List<String> authority = (List<String>) claims.get(AUTHORITIES_KEY);
        Collection<? extends GrantedAuthority> authorities =
                authority.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        UserDetails userDetails = new User(claims.getSubject(), "", authorities);
        // 일반 로그인 시 주로 이거로 인증 처리해서 SecurityContextHolder에 저장한다.
        // 시큐리티에서 인증을 나타내는 객체로 사용된다.
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    // 토큰 복호화
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token ={}", e.getClaims());
            return e.getClaims();
        }
    }

    // 토큰 검증을 위한 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            log.error("잘못된 JWT token ={}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token ={}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token ={}", e.getMessage());
        }
        return false;
    }

}
