package com.example.meettify.dto.meet;

import com.example.meettify.dto.meet.category.Category;
import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MeetDetailDTO {
    private Long meetId;
    private String meetName;
    private String meetDescription;
    private Long meetMaximum;
    private String meetLocation;
    private List<String> images;
    private Category category;

}
