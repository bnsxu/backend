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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Builder
public class UpdateMeetServiceDTO {
    private Long meetId;
    private String meetName;
    private String meetDescription;
    private Long meetMaximum;
    private String meetLocation;
    private List<String> existingImageUrls; // 기존 이미지 URL 리스트 추가
    private Category category;

    public static UpdateMeetServiceDTO makeServiceDTO(UpdateMeetDTO meet) {
        return UpdateMeetServiceDTO.builder()
                .meetId(meet.getMeetId())
                .meetName(meet.getMeetName())
                .meetDescription(meet.getMeetDescription())
                .meetMaximum(meet.getMeetMaximum())
                .meetLocation(meet.getMeetLocation())
                .existingImageUrls(meet.getExigistingImages())
                .category(meet.getCategory())
                .build();
    }

}
