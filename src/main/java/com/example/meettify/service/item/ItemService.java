package com.example.meettify.service.item;

import com.example.meettify.dto.item.CreateItemServiceDTO;
import com.example.meettify.dto.item.ResponseItemDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {
    // 상품 등록
    ResponseItemDTO createItem(CreateItemServiceDTO item,
                               List<MultipartFile> files,
                               String memberEmail);

}
