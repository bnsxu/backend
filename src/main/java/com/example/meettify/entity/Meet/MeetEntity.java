package com.example.meettify.entity.Meet;


import com.example.meettify.config.auditing.entity.BaseEntity;
import com.example.meettify.entity.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/*
 *   worker  : 조영흔
 *   work    : 모임 관련 정보를 저장하는 테이블을 생성해주는 엔티티 클래스
 *   date    : 2024/09/19
 * */
@Entity(name = "meets")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MeetEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meet_id")
    private Long meetId;

    @Column(name = "meet_name", nullable = false)
    private Long meetName;

    @Column(name = "meet_description", nullable = true)
    private String meetDescription;

    @Column(name = "meet_maximum", nullable = false)
    private Long meetMaximum;

    @Column(name = "meet_location", nullable = false)
    private String meetLocation;

    @OneToMany(mappedBy = "meetEntity", fetch = FetchType.LAZY)
    private List<MeetImageEntity> meetBoardImages;

    @Column(name = "meet_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category meetCategory;

    @OneToMany(mappedBy = "meetEntity")
    private List<MeetBoardEntity> MeetBoardEntity;
}
