package com.example.meettify.entity.member;

import com.example.meettify.config.auditing.entity.BaseEntity;
import com.example.meettify.dto.member.MemberServiceDTO;
import com.example.meettify.dto.member.UpdateMemberServiceDTO;
import com.example.meettify.dto.member.role.UserRole;
import jakarta.persistence.*;
import lombok.*;

/*
*   worker  : 유요한
*   work    : 유저에 관한 테이블을 생성해주는 엔티티 클래스
*   date    : 2024/09/27
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


    public static MemberEntity createMember(MemberServiceDTO member, String encodePw) {
        return MemberEntity.builder()
                .memberName(member.getMemberName())
                .memberEmail(member.getMemberEmail())
                .memberPw(encodePw)
                .nickName(member.getNickName())
                .memberRole(UserRole.USER)
                .address(AddressEntity.builder()
                        .memberAddr(member.getMemberAddr().getMemberAddr())
                        .memberAddrDetail(member.getMemberAddr().getMemberAddrDetail())
                        .memberZipCode(member.getMemberAddr().getMemberZipCode())
                        .build()).build();
    }

    public void updateMember(UpdateMemberServiceDTO member, String encodePw) {
        this.memberPw = encodePw == null ? this.memberPw : encodePw;
        this.nickName = member.getNickName() == null ? this.nickName : member.getNickName();

        // 기존 주소 엔티티를 직접 수정
        if (member.getMemberAddr() != null) {
            this.address = AddressEntity.builder()
                    .memberAddr(member.getMemberAddr().getMemberAddr())
                    .memberAddrDetail(member.getMemberAddr().getMemberAddrDetail())
                    .memberZipCode(member.getMemberAddr().getMemberZipCode())
                    .build();
        }
    }
}
