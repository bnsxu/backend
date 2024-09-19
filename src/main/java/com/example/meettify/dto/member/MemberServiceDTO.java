package com.example.meettify.dto.member;

import com.example.meettify.dto.member.role.UserRole;
import lombok.*;

/*
*   worker  : 유요한
*   work    : 서비스에 데이터를 보내주는 용도의 클래스
*             -> 객체지향적인 개발을 하기위해서 이렇게 하면 유연성이 증가하여 요청 데이터가 변해도
*                서비스 로직은 변경되지 않는다.
* */

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberServiceDTO {
    private String memberEmail;
    private String memberName;
    private String nickName;
    private String memberPw;
    private UserRole memberRole;
    private AddressDTO memberAddr;

    public static MemberServiceDTO makeServiceDTO(RequestMemberDTO request) {
        AddressDTO address =
                request.getMemberAddr() != null ? request.getMemberAddr() : new AddressDTO("", "", "");
        return MemberServiceDTO.builder()
                .memberEmail(request.getMemberEmail())
                .memberName(request.getMemberName())
                .nickName(request.getNickName())
                .memberPw(request.getMemberPw())
                .memberRole(request.getMemberRole())
                .memberAddr(request.getMemberAddr())
                .build();
    }
}
