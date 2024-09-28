package com.example.meettify.dto.jwt;

import com.example.meettify.entity.jwt.TokenEntity;
import lombok.*;
/*
*   writer  : 유요한
*   work    : 로그인 시 프론트에게 JWT를 발급하기 위한 용도
*   date    : 2024/09/27
* */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Builder
public class TokenDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private String memberEmail;
    private Long memberId;


    public static TokenDTO changeDTO(TokenEntity token, String accessToken) {
        return TokenDTO.builder()
                .grantType(token.getGrantType())
                .accessToken(accessToken)
                .refreshToken(token.getRefreshToken())
                .memberEmail(token.getEmail())
                .memberId(token.getMemberId())
                .build();
    }
}
