package com.example.meettify.entity.Meet;

import com.example.meettify.config.auditing.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/*
 *   worker  : 조영흔
 *   work    : 모임 게시물의 이미지 테이블 추가를 위한 엔티티
 *   date    : 2024/09/19
 * */
@Entity(name = "meetBoardImages")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MeetBoardImageEntity extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name="meetBoardImage_id")
    private Long meetBoardImagesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meetBoard_id")
    private MeetBoardEntity meetBoardEntity;

    @Column(name="images")
    private String images;
}
