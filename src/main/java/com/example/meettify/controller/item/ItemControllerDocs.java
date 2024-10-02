package com.example.meettify.controller.item;

import com.example.meettify.dto.item.CreateItemDTO;
import com.example.meettify.dto.item.UpdateItemDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "item", description = "상퓸 API")
public interface ItemControllerDocs {
    @Operation(summary = "상품 등록", description = "상품 등록하는 API")
    ResponseEntity<?> createItem(CreateItemDTO item,
                                 List<MultipartFile> files,
                                 BindingResult bindingResult,
                                 UserDetails userDetails);

    @Operation(summary = "상품 수정", description = "상품 수정하는 API")
    ResponseEntity<?> updateItem(Long itemId,
                                 UpdateItemDTO item,
                                 List<MultipartFile> files,
                                 UserDetails userDetails);
}
