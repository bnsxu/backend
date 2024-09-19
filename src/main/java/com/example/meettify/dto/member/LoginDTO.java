package com.example.meettify.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class LoginDTO {
    @Schema(description = "이메일", example = "test@test.com")
    private String email;
    @Schema(description = "회원 비밀번호")
    private String memberPw;
}
