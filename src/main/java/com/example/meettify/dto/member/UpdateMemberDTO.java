package com.example.meettify.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class UpdateMemberDTO {
    @Schema(description = "회원 닉네임")
    @NotNull(message = "닉네임은 필수입니다.")
    private String nickName;

    @Schema(description = "회원 비밀번호")
    private String memberPw;

    @Schema(description = "회원 주소")
    @NotNull(message = "주소는 필수입니다.")
    private AddressDTO memberAddr;
}
