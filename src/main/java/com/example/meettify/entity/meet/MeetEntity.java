package com.example.meettify.entity.meet;


import com.example.meettify.config.auditing.entity.BaseEntity;
import com.example.meettify.dto.meet.UpdateMeetServiceDTO;
import com.example.meettify.dto.meet.category.Category;
import com.example.meettify.entity.meetBoard.MeetBoardEntity;
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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meet_id")
    private Long meetId;

    @Column(name = "meet_name", nullable = false)
    private String meetName;

    @Column(name = "meet_description", nullable = true)
    private String meetDescription;

    @Column(name = "meet_maximum", nullable = false)
    private Long meetMaximum;

    @Column(name = "meet_location", nullable = false)
    private String meetLocation;

    @OneToMany(mappedBy = "meetEntity", fetch = FetchType.LAZY)
    private List<com.example.meettify.entity.meet.MeetImageEntity> meetImages;

    @Column(name = "meet_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category meetCategory;

    @OneToMany(mappedBy = "meetEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetBoardEntity> MeetBoardEntity;


    public void updateMeet(UpdateMeetServiceDTO updateMeetDTO) {
        // 변경 요구사항을 가지고 해당 메소드 실행
        this.meetName = updateMeetDTO.getMeetName() == null ? this.meetName : updateMeetDTO.getMeetName();
        this.meetCategory = updateMeetDTO.getCategory() == null ? this.meetCategory : updateMeetDTO.getCategory();
        this.meetLocation = updateMeetDTO.getMeetLocation() == null ? this.meetDescription : updateMeetDTO.getMeetDescription();
        // 새 이미지 -> 등록
        // 기존 이미지 -> 전달 받은 거 유지, 전달 안 받은 거 삭제
        this.meetMaximum = updateMeetDTO.getMeetMaximum() == null ? this.meetMaximum : updateMeetDTO.getMeetMaximum();
        this.meetDescription = updateMeetDTO.getMeetDescription() == null ? this.meetDescription : updateMeetDTO.getMeetDescription();
    }

}
