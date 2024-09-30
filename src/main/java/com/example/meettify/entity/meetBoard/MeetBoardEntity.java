package com.example.meettify.entity.meetBoard;

import com.example.meettify.config.auditing.entity.BaseEntity;
import com.example.meettify.dto.meetBoard.MeetBoardServiceDTO;
import com.example.meettify.dto.meetBoard.UpdateMeetBoardServiceDTO;
import com.example.meettify.entity.meet.MeetEntity;
import com.example.meettify.entity.member.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/*
 *   worker  : 조영흔
 *   work    : 모임 게시물 관련 정보를 저장하기 위한 엔티티 클래스
 *   date    : 2024/09/19
 * */
@Entity(name = "meetBoards")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MeetBoardEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meetBoard_id")
    private Long meetBoardId;

    @Column(name="meetBoard_title", nullable = false)
    private String meetBoardTitle;

    @Column(name="meetBoard_content")
    private String meetBoardContent;

    @OneToMany(mappedBy = "meetBoardEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetBoardImageEntity> meetBoardImages;

    @JoinColumn(name = "member_id")
    @ManyToOne( fetch = FetchType.LAZY)
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="meet_id")
    private MeetEntity meetEntity;

    @Column(name="meetBoard_postDate")
    private LocalDateTime postDate;

    @PrePersist
    public void prePersist() {
        this.postDate = (this.postDate == null) ? LocalDateTime.now() : this.postDate;
    }

    public void addMeetBoardImage(MeetBoardImageEntity imageEntity) {
        this.meetBoardImages.add(imageEntity);
        imageEntity.setMeetBoardEntity(this);  // Bidirectional link
    }

    public static MeetBoardEntity postMeetBoard(MeetBoardServiceDTO meetBoardServiceDTO, MemberEntity member, MeetEntity meetEntity){
        return MeetBoardEntity.builder()
                .meetBoardTitle(meetBoardServiceDTO.getMeetBoardTitle())
                .meetBoardContent(meetBoardServiceDTO.getMeetBoardContent())
                .memberEntity(member)  // 작성자 정보 설정
                .meetEntity(meetEntity)  // 모임 정보 설정
                .build();
    }

    public void updateMeet(UpdateMeetBoardServiceDTO updateMeetBoardServiceDTO) {
        // 변경 요구사항을 가지고 해당 메소드 실행
        this.meetBoardTitle = updateMeetBoardServiceDTO.getMeetBoardTitle() == null ? this.getMeetBoardTitle() : updateMeetBoardServiceDTO.getMeetBoardTitle();
        this.meetBoardContent = updateMeetBoardServiceDTO.getMeetBoardContent() == null ? this.getMeetBoardContent() : updateMeetBoardServiceDTO.getMeetBoardContent();

    }
}
