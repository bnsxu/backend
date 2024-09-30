package com.example.meettify.dto.meetBoard;


import com.example.meettify.entity.meetBoard.MeetBoardEntity;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MeetBoardSummaryDTO {
    private Long meetBoardId;
    private String title;
    private String nickName;
    private LocalDateTime postDate;


    public static MeetBoardSummaryDTO changeDTO(MeetBoardEntity meetBoardEntity){
        return MeetBoardSummaryDTO.builder()
                .nickName(meetBoardEntity.getMemberEntity().getNickName())
                .meetBoardId(meetBoardEntity.getMeetBoardId())
                .title(meetBoardEntity.getMeetBoardTitle())
                .postDate(meetBoardEntity.getPostDate())
                .build();
    }
}
