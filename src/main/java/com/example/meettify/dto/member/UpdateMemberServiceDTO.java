package com.example.meettify.dto.member;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Builder
public class UpdateMemberServiceDTO {
    private String nickName;
    private String memberPw;
    private AddressDTO memberAddr;


    public static UpdateMemberServiceDTO makeServiceDTO(UpdateMemberDTO member) {
        AddressDTO address =
                member.getMemberAddr() != null ? member.getMemberAddr() : new AddressDTO("", "", "");
        return UpdateMemberServiceDTO.builder()
                .nickName(member.getNickName())
                .memberPw(member.getMemberPw())
                .memberAddr(member.getMemberAddr())
                .build();
    }
}
