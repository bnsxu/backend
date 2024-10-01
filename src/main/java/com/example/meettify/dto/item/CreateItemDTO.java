package com.example.meettify.dto.item;

import com.example.meettify.dto.item.status.ItemStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/*
 *   writer  : 유요한
 *   work    : 상품을 생성할 때 요청을 담기 위한 클래스
 *   date    : 2024/09/30
 * */
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CreateItemDTO {
    @Schema(description = "상품 이름")
    @NotNull(message = "상품 이름은 필수입니다.")
    private Long itemName;

    @Schema(description = "상품 가격")
    @NotNull(message = "상품 가격은 필수입니다.")
    @Min(value = 0, message = "상품 가격은 최소 0원 이상이여야 합니다.")
    private int itemPrice;

    @Schema(description = "상품 설명")
    @NotNull(message = "상품 설명은 필수입니다.")
    private String itemDetails;

    @Schema(description = "상품 상태")
    @NotNull(message = "상품 상태는 필수입니다.")
    private ItemStatus itemStatus;

    @Schema(description = "상품 개수")
    @NotNull(message = "상품 개수는 필수입니다.")
    @Min(value = 1, message = "상품 개수는 최소 1개 이상이여야 합니다.")
    private int itemCount;
}
