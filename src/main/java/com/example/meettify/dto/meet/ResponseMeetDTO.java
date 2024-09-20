package com.example.meettify.dto.meet;


import com.example.meettify.dto.meet.category.Category;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private String[] images;
    private Category category;
}
