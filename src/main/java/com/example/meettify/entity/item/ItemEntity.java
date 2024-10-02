package com.example.meettify.entity.item;

import com.example.meettify.config.auditing.entity.BaseEntity;
import com.example.meettify.dto.item.CreateItemServiceDTO;
import com.example.meettify.dto.item.UpdateItemDTO;
import com.example.meettify.dto.item.UpdateItemServiceDTO;
import com.example.meettify.dto.item.status.ItemStatus;
import com.example.meettify.dto.meet.category.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 *   writer  : 유요한
 *   work    : 상품 정보를 담아줄 엔티티
 *   date    : 2024/09/30
 * */
@Entity(name = "items")
@Getter
@ToString(exclude = {"images"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ItemEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_price")
    private int itemPrice;

    @Column(name = "item_details")
    private String itemDetails;

    @Column(name = "item_status")
    private ItemStatus itemStatus;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("itemImgId asc ")
    @Builder.Default
    private List<ItemImgEntity> images = new ArrayList<>();

    // 재고 수량
    @Column(name = "item_count")
    private int itemCount;

    @Column(name = "item_category")
    private Category itemCategory;


    // 상품 엔티티 생성
    public static ItemEntity createEntity(CreateItemServiceDTO item) {
        return ItemEntity.builder()
                .itemName(item.getItemName())
                .itemPrice(item.getItemPrice())
                .itemDetails(item.getItemDetails())
                .itemStatus(item.getItemStatus())
                .itemCount(item.getItemCount())
                .itemCategory(item.getItemCategory())
                .build();
    }

    public void addImage(ItemImgEntity image) {
        this.images.add(image);
    }

    public void updateItem(UpdateItemServiceDTO item, List<ItemImgEntity> images) {
        this.itemName = Optional.ofNullable(item.getItemName()).orElse(this.getItemName());
        this.itemDetails = Optional.ofNullable(item.getItemDetail()).orElse(this.getItemDetails());
        this.itemPrice = item.getPrice() != 0 ? item.getPrice() : this.getItemPrice();  // 기본형은 null이 아닌 0으로 초기화됨
        this.itemCount = item.getItemCount() != 0 ? item.getItemCount() : this.getItemCount();
        this.itemStatus = Optional.ofNullable(item.getItemStatus()).orElse(this.getItemStatus());
        this.itemCategory = Optional.ofNullable(item.getCategory()).orElse(this.getItemCategory());

        // this.images가 null이면 새로운 ArrayList로 초기화
        if (this.images == null) {
            this.images = new ArrayList<>();
        }

        // 새로 전달된 이미지가 있을 때만 기존 이미지에 합침
        // 새로 전달된 이미지가 있을 경우에만 추가
        if (images != null && !images.isEmpty()) {
            for (ItemImgEntity image : images) {
                // 중복된 이미지를 방지하여 추가
                if (!this.images.contains(image)) {
                    this.images.add(image);
                }
            }
        }
    }

    public void remainImgId(List<Long> remainImgId) {
        this.images.removeIf(img -> !remainImgId.contains(img.getItemImgId()));
    }
}
