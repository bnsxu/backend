package com.example.meettify.entity.item;

import com.example.meettify.config.auditing.entity.BaseEntity;
import com.example.meettify.dto.item.CreateItemServiceDTO;
import com.example.meettify.dto.item.status.ItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private Long itemName;

    @Column(name = "item_price")
    private int itemPrice;

    @Column(name = "item_details")
    private String itemDetails;

    @Column(name = "item_status")
    private ItemStatus itemStatus;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("itemImgId asc ")
    private List<ItemImgEntity> images = new ArrayList<>();

    // 재고 수량
    @Column(name = "item_count")
    private int itemCount;

    // 상품 엔티티 생성
    public static ItemEntity createEntity(CreateItemServiceDTO item) {
        return ItemEntity.builder()
                .itemName(item.getItemName())
                .itemPrice(item.getItemPrice())
                .itemDetails(item.getItemDetails())
                .itemStatus(item.getItemStatus())
                .itemCount(item.getItemCount())
                .build();
    }

    public void addImage(ItemImgEntity image) {
        this.images.add(image);
    }
}
