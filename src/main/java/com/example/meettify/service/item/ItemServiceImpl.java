package com.example.meettify.service.item;

import com.example.meettify.config.s3.FileDTOFactory;
import com.example.meettify.config.s3.S3ImageUploadService;
import com.example.meettify.dto.item.CreateItemServiceDTO;
import com.example.meettify.dto.item.ResponseItemDTO;
import com.example.meettify.dto.item.ResponseItemImgDTO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Log4j2
public class ItemServiceImpl implements ItemService{
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final S3ImageUploadService s3ImageUploadService;
    private final FileDTOFactory<ResponseItemImgDTO> fileDTOFactory; // 파일 DTO 생성 팩토리


    @Override
    public ResponseItemDTO createItem(CreateItemServiceDTO item, List<MultipartFile> files, String memberEmail) {
        try {
            MemberEntity findMember = memberRepository.findByMemberEmail(memberEmail);
            if (findMember != null) {
                List<ResponseItemImgDTO> itemImages = s3ImageUploadService.upload("product", files, fileDTOFactory);
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
}
