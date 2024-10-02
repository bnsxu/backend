package com.example.meettify.dto.item;

import com.example.meettify.dto.item.status.ItemStatus;
import com.example.meettify.dto.meet.category.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class UpdateItemDTO {
    @Schema(description = "상품명")
    private String itemName;        // 상품 명
    @Schema(description = "상품 가격")
    private int price;              // 가격
    @Schema(description = "상품 상세 설명")
    private String itemDetail;      // 상품 상세 설명
    @Schema(description = "상품 수량")
    private int itemCount;          // 재고수량
    @Schema(description = "남길 상품 이미지")
    private List<Long> remainImgId; // 남길 이미지
    @Schema(description = "상품 상태")
    private ItemStatus itemStatus;  // 상품 상태
    @Schema(description = "상품 카테고리")
    private Category category;      // 상품 카테고리
}
