package com.example.meettify.dto.meet;

import com.example.meettify.dto.meet.category.Category;
import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MeetSummaryDTO {
    private Long meetId;
    private String meetName;
    private String location;
    private Category category;
    private Long maximum;
    private List<String> imageUrls;
}

