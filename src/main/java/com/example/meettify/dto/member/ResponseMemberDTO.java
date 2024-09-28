package com.example.meettify.dto.member;

import com.example.meettify.dto.member.role.UserRole;
import com.example.meettify.entity.member.MemberEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/*
 *   worker  : 유요한
 *   work    : 프론트에게 보내줄 response
 *   date    : 2024/09/27
 * */
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ResponseMemberDTO {
    private Long memberId;
    private String memberEmail;
    private String memberName;
    private String nickName;
    private String memberPw;
    private UserRole memberRole;
    private AddressDTO memberAddr;

    public static ResponseMemberDTO changeDTO(MemberEntity member) {
        return ResponseMemberDTO.builder()
                .memberId(member.getMemberId())
                .memberEmail(member.getMemberEmail())
                .memberName(member.getMemberName())
                .memberPw(member.getMemberPw())
                .memberRole(member.getMemberRole())
                .memberAddr(AddressDTO.builder()
                        .memberAddr(member.getAddress().getMemberAddr())
                        .memberAddrDetail(member.getAddress().getMemberAddrDetail())
                        .memberZipCode(member.getAddress().getMemberZipCode())
                        .build())
                .build();
    }
}
