package com.example.meettify.dto.meet;

import com.example.meettify.dto.meetBoard.MeetBoardSummaryDTO;
import lombok.*;

import java.util.List;


@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MeetDetailInfoResponseDTO {
    Long meetId;
    MeetDetailDTO meetDetailDTO;
    MeetRole meetRole;
    List<MeetBoardSummaryDTO> meetBoardSummaryDTOList;
}
