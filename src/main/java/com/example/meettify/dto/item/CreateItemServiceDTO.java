package com.example.meettify.dto.item;

import com.example.meettify.dto.item.status.ItemStatus;
import com.example.meettify.dto.meet.category.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/*
 *   writer  : 유요한
 *   work    : 서비스로 보내주기 위한 클래스
 *   date    : 2024/09/30
 * */
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CreateItemServiceDTO {
    @NotNull(message = "상품 이름은 필수입니다.")
    private String itemName;

    @NotNull(message = "상품 가격은 필수입니다.")
    @Min(value = 0, message = "상품 가격은 최소 0원 이상이여야 합니다.")
    private int itemPrice;

    @NotNull(message = "상품 설명은 필수입니다.")
    private String itemDetails;

    @NotNull(message = "상품 상태는 필수입니다.")
    private ItemStatus itemStatus;

    @NotNull(message = "상품 개수는 필수입니다.")
    @Min(value = 1, message = "상품 개수는 최소 1개 이상이여야 합니다.")
    private int itemCount;

    @NotNull(message = "상품 카테고리는 필수입니다.")
    private Category itemCategory;
}
