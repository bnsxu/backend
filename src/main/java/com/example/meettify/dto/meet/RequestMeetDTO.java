package com.example.meettify.dto.meet;


import com.example.meettify.dto.meet.category.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class RequestMeetDTO {
    @Schema(description = "모임 이름", example = "런닝 크루")
    @NotNull(message = "모임명은 필수입니다.")
    @Pattern(regexp="^[\\S\\s]{1,25}$", message = "모임명은 1자 이상 25자 이하이어야 합니다.")
    private String meetName;

    @Schema(description = "모임 설명", example = "저희는 활력적인 삶을 살기 위해서 Runnging모임을 하는 크루입니다.")
    @NotNull(message = "컨텐츠 내용은 필수입니다.")
    @Pattern(regexp="^[\\S\\s]{1,1500}$", message = "컨텐츠은 1자 이상 1500자 이하이어야 합니다.")
    private String meetDescription;

    @Schema(description = "모임 전체 인원수", example = "30")
    @NotNull(message = "모임 인원수 입력은 필수 입니다.")
    @Min(value = 1, message = "모임 인원수는 최소 1명이어야 합니다.")
    @Max(value = 200, message = "모임 인원수는 최대 200명이어야 합니다.")
    private Long meetMaximum;

    @Schema(description = "모임 지역", example = "서울 종로구")
    @NotNull(message = "모임 지역은 필수입니다.")
    private String meetLocation;

    @Schema(description = "모임 이미지 리스트", example = "main.jpg")
    private MultipartFile[] images;  // 이미지를 멀티파트 파일로 처리

    @Schema(description = "카테고리 정하기", example = "운동")
    @NotNull(message = "모임 카테고리는 필수입니다.")
    private Category category;
}
