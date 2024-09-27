package com.example.meettify.dto.meetBoard;


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
}
