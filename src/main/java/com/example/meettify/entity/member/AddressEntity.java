package com.example.meettify.entity.member;

import jakarta.persistence.Embeddable;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Embeddable
public class AddressEntity {
    private String memberAddr;
    private String memberAddrDetail;
    private String memberZipCode;
}
