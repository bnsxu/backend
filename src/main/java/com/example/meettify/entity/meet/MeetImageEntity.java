package com.example.meettify.entity.meet;

import com.example.meettify.config.auditing.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/*
 *   worker  : 조영흔
 *   work    : 모임 이미지 테이블 추가를 위한 엔티티
 *   date    : 2024/09/19
 * */
@Entity(name = "meetImages")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MeetImageEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="meetIamge_id")
    private Long meetImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meet_id")
    private MeetEntity meetEntity;

    @Column(name="meetImage_oriFileName")
    String oriFileName;
    @Column(name="meetImage_uploadFileName")
    String uploadFileName;
    @Column(name="meetImage_uploadFilePath")
    String uploadFilePath;
    @Column(name="meetImage_uploadFileUrl")
    String uploadFileUrl;




}
