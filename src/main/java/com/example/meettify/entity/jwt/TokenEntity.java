package com.example.meettify.entity.jwt;

import com.example.meettify.dto.jwt.TokenDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "tokens")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class TokenEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;
    private String grantType;
    private String refreshToken;
    private String email;
    private Long memberId;

    // 토큰 엔티티로 변환
    // accessToken이 없는 이유는 DB에 저장하지 않고 클라이언트에게 보내서 클라이언트에서 관리하게 하기 위해서
    // accessToken은 만료가 짧기 때문에 굳이 DB에 저장할 필요가 없다.
    public static TokenEntity changeEntity(TokenDTO token) {
        return TokenEntity.builder()
                .grantType(token.getGrantType())
                .refreshToken(token.getRefreshToken())
                .email(token.getMemberEmail())
                .memberId(token.getMemberId())
                .build();
    }

    // 토큰 업데이트
    public void updateToken(TokenDTO token) {
        this.refreshToken = token.getRefreshToken();
    }
}
