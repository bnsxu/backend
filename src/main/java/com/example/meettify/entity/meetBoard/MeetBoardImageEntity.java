package com.example.meettify.entity.meetBoard;

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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="meetBoardImage_id")
    private Long meetBoardImagesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name = "meetBoard_id")
    private MeetBoardEntity meetBoardEntity;

    @Column(name="meetBoardImage_oriFileName")
    String oriFileName;
    @Column(name="meetBoardImage_uploadFileName")
    String uploadFileName;
    @Column(name="meetBoardImage_uploadFilePath")
    String uploadFilePath;
    @Column(name="meetBoardImage_uploadFileUrl")
    String uploadFileUrl;

}
