package com.example.meettify.entity.member;

import com.example.meettify.config.auditing.entity.BaseEntity;
import com.example.meettify.dto.member.MemberUpdateServiceDTO;
import com.example.meettify.dto.member.UpdateMemberDTO;
import com.example.meettify.dto.member.role.UserRole;
import jakarta.persistence.*;
import lombok.*;

/*
*   worker  : 유요한
*   work    : 유저에 관한 테이블을 생성해주는 엔티티 클래스
*   date    : 2024/09/19
* */

@Entity(name = "members")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;
    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_email", nullable = false)
    private String memberEmail;

    @Column(name = "member_pw")
    private String memberPw;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    // USER, ADMIN
    private UserRole memberRole;

    @Embedded
    private AddressEntity address;

    private String provider;
    private String providerId;

    public void updateMember(MemberUpdateServiceDTO updateMemberDTO, String password) {
        this.memberPw = updateMemberDTO.getMemberPw() == null ? this.memberPw : updateMemberDTO.getMemberPw();
        this.nickName = updateMemberDTO.getNickName() == null ? this.nickName : updateMemberDTO.getNickName();

        // 기존 주소 엔티티를 직접 수정
        if (updateMemberDTO.getMemberAddr() != null) {
            this.address = AddressEntity.builder()
                    .memberAddr(updateMemberDTO.getMemberAddr().getMemberAddr())
                    .memberAddrDetail(updateMemberDTO.getMemberAddr().getMemberAddrDetail())
                    .memberZipCode(updateMemberDTO.getMemberAddr().getMemberZipCode())
                    .build();
        }
    }
}
