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
public class UpdateItemServiceDTO {
    @NotNull(message = "상품명은 필수 입력입니다.")
    private String itemName;     // 상품 명

    @NotNull(message = "가격은 필수 입력입니다.")
    private int price;          // 가격

    @NotNull(message = "설명은 필수 입력입니다.")
    private String itemDetail;  // 상품 상세 설명

    @NotNull(message = "재고 수량은 필수 입력입니다.")
    private int itemCount;    // 재고수량

    @NotNull(message = "남길 이미지를 입력해주셔야 합니다. 공백으로라도 보내주세요.")
    private List<Long> remainImgId;

    private ItemStatus itemStatus;  // 상품 상태
    private Category category;      // 상품 카테고리
}
