package com.example.meettify.dto.meet;


import com.example.meettify.dto.meet.category.Category;
import com.example.meettify.entity.meet.MeetEntity;
import com.example.meettify.entity.meet.MeetImageEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/*
 *   worker  : 조영흔
 *   work    : 모임 게시물 관련 정보를 저장하기 위한 엔티티 클래스
 *   date    : 2024/09/30
 * */
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ResponseMeetDTO {
    private Long meetId;
    private String meetName;
    private String meetDescription;
    private Long meetMaximum;
    private String meetLocation;
    private List<String> images;
    private Category category;

    public static ResponseMeetDTO changeDTO(MeetEntity meetEntity) {
        return ResponseMeetDTO.builder()
                .meetId(meetEntity.getMeetId())
                .meetName(meetEntity.getMeetName())
                .meetDescription(meetEntity.getMeetDescription())
                .meetMaximum(meetEntity.getMeetMaximum())
                .meetLocation(meetEntity.getMeetLocation())
                .images(meetEntity.getMeetImages().stream().map(MeetImageEntity::getUploadFileUrl).collect(Collectors.toList()))
                .category(meetEntity.getMeetCategory())
                .build();
    }

}
