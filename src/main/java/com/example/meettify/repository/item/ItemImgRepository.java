package com.example.meettify.repository.item;

import com.example.meettify.entity.item.ItemImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImgEntity, Long> {
    List<ItemImgEntity> findByItem_ItemId(Long itemId);
}
