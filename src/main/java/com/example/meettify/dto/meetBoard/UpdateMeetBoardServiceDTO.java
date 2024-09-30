package com.example.meettify.dto.meetBoard;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/*
 *   worker  : 조영흔
 *   work    : 서비스에 데이터를 보내주는 용도의 클래스
 *             -> 객체지향적인 개발을 하기위해서 이렇게 하면 유연성이 증가하여 요청 데이터가 변해도
 *                서비스 로직은 변경되지 않는다.
 *   date    : 2024/09/30
 * */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UpdateMeetBoardServiceDTO {

    private Long meetBoardId;
    String meetBoardTitle;
    String meetBoardContent;
    private List<MultipartFile> images;
    private List<String> imagesUrl;


    public static UpdateMeetBoardServiceDTO makeServiceDTO(UpdateRequestMeetBoardDTO updateRequestMeetBoardDTO){
        return UpdateMeetBoardServiceDTO.builder()
                .meetBoardId(updateRequestMeetBoardDTO.getMeetBoardId())
                .meetBoardTitle(updateRequestMeetBoardDTO.getMeetBoardTitle())
                .meetBoardContent(updateRequestMeetBoardDTO.getMeetBoardContent())
                .images(updateRequestMeetBoardDTO.getImages())
                .imagesUrl(updateRequestMeetBoardDTO.getImagesUrl())
                .build();
    }
}
