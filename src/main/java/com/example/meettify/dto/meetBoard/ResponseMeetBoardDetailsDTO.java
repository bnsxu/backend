package com.example.meettify.dto.meetBoard;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/*
 *   worker  : 조영흔
 *   work    : 모임 게시판 상세 정보를 표현하기 위한 DTO
 *   date    : 2024/09/26
 * */
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ResponseMeetBoardDetailsDTO {
    private Long meetBoardId;
    private Long  meetId;
    private String meetBoardTitle;
    private String meetBoardContent;
    private LocalDateTime postDate;
    @Setter
    private List<String> images;

}
