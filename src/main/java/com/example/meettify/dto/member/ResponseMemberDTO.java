package com.example.meettify.dto.member;

import com.example.meettify.dto.member.role.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ResponseMemberDTO {
    private Long memberId;
    private String email;
    private String memberName;
    private String nickName;
    private String memberPw;
    private UserRole memberRole;
    private AddressDTO memberAddr;
}
