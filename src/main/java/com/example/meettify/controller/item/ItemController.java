package com.example.meettify.controller.item;

import com.example.meettify.dto.item.CreateItemDTO;
import com.example.meettify.dto.item.CreateItemServiceDTO;
import com.example.meettify.dto.item.ResponseItemDTO;
import com.example.meettify.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController implements ItemControllerDocs{
    private final ItemService itemService;
    private final ModelMapper modelMapper;

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createItem(@Validated @RequestPart CreateItemDTO item,
                                        @RequestPart(value = "files", required = false)List<MultipartFile> files,
                                        BindingResult bindingResult,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult);
            }

            CreateItemServiceDTO changeServiceDTO = modelMapper.map(item, CreateItemServiceDTO.class);

            String email = userDetails.getUsername();
            ResponseItemDTO response = itemService.createItem(changeServiceDTO, files, email);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
