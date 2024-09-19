package com.example.meettify.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Builder
public class MemberUpdateServiceDTO {
    private String nickName;
    private String memberPw;
    private AddressDTO memberAddr;


    public static MemberUpdateServiceDTO makeServiceDTO(UpdateMemberDTO member) {
        AddressDTO address =
                member.getMemberAddr() != null ? member.getMemberAddr() : new AddressDTO("", "", "");
        return MemberUpdateServiceDTO.builder()
                .nickName(member.getNickName())
                .memberPw(member.getMemberPw())
                .memberAddr(member.getMemberAddr())
                .build();
    }
}
