package com.example.meettify.dto.member;

import com.example.meettify.entity.member.MemberEntity;
import lombok.*;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class SocialDTO {
    private Long id;
    private String memberEmail;
    private String provider;
    private String providerId;

    public static SocialDTO changeDTO(MemberEntity member) {
        return SocialDTO.builder()
                .id(member.getMemberId())
                .memberEmail(member.getMemberEmail())
                .provider(member.getProvider())
                .providerId(member.getProviderId())
                .build();
    }
}
