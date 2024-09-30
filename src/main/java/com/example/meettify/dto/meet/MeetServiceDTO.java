package com.example.meettify.dto.meet;

import com.example.meettify.dto.meet.category.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/*
*   worker  : 유요한
*   work    : 서비스에 데이터를 보내주는 용도의 클래스
*             -> 객체지향적인 개발을 하기위해서 이렇게 하면 유연성이 증가하여 요청 데이터가 변해도
*                서비스 로직은 변경되지 않는다.
*  date :   : 2024/09/20
* */


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MeetServiceDTO {
    private String meetName;
    private String meetDescription;
    private Long meetMaximum;
    private String meetLocation;
    private List<MultipartFile> imagesFile;
    private Category category;

    public static MeetServiceDTO makeServiceDTO(RequestMeetDTO request) {
        return MeetServiceDTO.builder()
                .meetName(request.getMeetName())
                .meetDescription(request.getMeetDescription())
                .meetMaximum(request.getMeetMaximum())
                .meetLocation(request.getMeetLocation())
                .imagesFile(request.getImages())
                .category(request.getCategory())
                .build();
    }

}
