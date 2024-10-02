package com.example.meettify.dto.item;

import com.example.meettify.dto.item.status.ItemStatus;
import com.example.meettify.dto.meet.category.Category;
import com.example.meettify.entity.item.ItemEntity;
import com.example.meettify.entity.item.ItemImgEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
 *   writer  : 유요한
 *   work    : 프론트에게 response해줄 상품 클래스
 *   date    : 2024/10/01
 * */
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
public class ResponseItemDTO {
    private Long itemId;
    private String itemName;
    private int itemPrice;
    private String itemDetails;
    private ItemStatus itemStatus;
    @Builder.Default
    private List<ResponseItemImgDTO> images = new ArrayList<>();
    // 재고 수량
    private int itemCount;
    private Category itemCategory;

    // 엔티티 -> DTO
    public static ResponseItemDTO changeDTO(ItemEntity item) {
        // 상품 엔티티에서 이미지들 가져옴
        List<ItemImgEntity> imagesEntity
                = item.getImages().isEmpty() ? Collections.emptyList() : item.getImages();
        // 가져온 이미지들을 DTO로 변환
        List<ResponseItemImgDTO> imagesDTO = imagesEntity.stream()
                .map(ResponseItemImgDTO::changeDTO)
                .collect(Collectors.toList());

        return ResponseItemDTO.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .itemPrice(item.getItemPrice())
                .itemDetails(item.getItemDetails())
                .itemStatus(item.getItemStatus())
                .images(imagesDTO)
                .itemCategory(item.getItemCategory())
                .itemCount(item.getItemCount())
                .build();
    }
}
