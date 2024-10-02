package com.example.meettify.service.item;

import com.example.meettify.config.s3.S3ImageUploadService;
import com.example.meettify.dto.item.CreateItemServiceDTO;
import com.example.meettify.dto.item.ResponseItemDTO;
import com.example.meettify.dto.item.ResponseItemImgDTO;
import com.example.meettify.dto.item.UpdateItemServiceDTO;
import com.example.meettify.entity.item.ItemEntity;
import com.example.meettify.entity.item.ItemImgEntity;
import com.example.meettify.entity.member.MemberEntity;
import com.example.meettify.exception.item.ItemException;
import com.example.meettify.exception.member.MemberException;
import com.example.meettify.repository.item.ItemImgRepository;
import com.example.meettify.repository.item.ItemRepository;
import com.example.meettify.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Transactional
@RequiredArgsConstructor
@Log4j2
@Service
public class ItemServiceImpl implements ItemService {
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final S3ImageUploadService s3ImageUploadService;


    @Override
    public ResponseItemDTO createItem(CreateItemServiceDTO item, List<MultipartFile> files, String memberEmail) {
        try {
            MemberEntity findMember = memberRepository.findByMemberEmail(memberEmail);
            if (findMember != null) {
                List<ResponseItemImgDTO> itemImages = uploadItemImages(files);
                ItemEntity itemEntity = ItemEntity.createEntity(item);
                ItemImgEntity imgEntity = ItemImgEntity.createEntity(itemImages, itemEntity);
                itemEntity.addImage(imgEntity);
                ItemEntity saveItem = itemRepository.save(itemEntity);
                return ResponseItemDTO.changeDTO(saveItem);
            }
            throw new MemberException("회원이 존재하지 않습니다.");
        } catch (Exception e) {
            throw new ItemException("상품 생성 실패 : " + e.getMessage());
        }
    }

    private List<ResponseItemImgDTO> uploadItemImages(List<MultipartFile> files) throws IOException {
        return s3ImageUploadService.upload("product", files, (oriFileName, uploadFileName, uploadFilePath, uploadFileUrl) ->
                ResponseItemImgDTO.builder()
                        .originalImgName(oriFileName)
                        .uploadImgName(uploadFileName)
                        .uploadImgPath(uploadFilePath)
                        .uploadImgUrl(uploadFileUrl)
                        .build()
        );
    }

    @Override
    public ResponseItemDTO updateItem(Long itemId,
                                      UpdateItemServiceDTO updateItemDTO,
                                      List<MultipartFile> files,
                                      String memberEmail,
                                      String role) {
        try {
            ItemEntity findItem = itemRepository.findById(itemId)
                    .orElse(null);
            List<ItemImgEntity> findItemImg = itemImgRepository.findByItem_ItemId(itemId);

            if(updateItemDTO.getRemainImgId().isEmpty()) {
                requireNonNull(findItem).getImages().clear();
            } else {
                requireNonNull(findItem).remainImgId(updateItemDTO.getRemainImgId());
            }

            findItem.updateItem(updateItemDTO, findItemImg);
            ItemEntity saveItem = itemRepository.save(findItem);
            return ResponseItemDTO.changeDTO(saveItem);
        } catch (Exception e) {
            throw new ItemException(e.getMessage());
        }
    }
}
