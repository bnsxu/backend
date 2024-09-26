package com.example.meettify.service.meetBoard;

import com.example.meettify.dto.meetBoard.MeetBoardDetailsResponseDTO;
import com.example.meettify.entity.Meet.MeetBoardEntity;
import com.example.meettify.entity.Meet.MeetBoardImageEntity;
import com.example.meettify.repository.meetBoard.MeetBoardImageRepository;
import com.example.meettify.repository.meetBoard.MeetBoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.Destination;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MeetBoardServiceImpl implements MeetBoardService {
    private final MeetBoardRepository meetBoardRepository;
    private final MeetBoardImageRepository meetBoardImageRepository;
    private final ModelMapper modelMapper;


    @Override
    public MeetBoardDetailsResponseDTO getDetails(Long meetBoardId) {
        try {
            MeetBoardEntity meetBoardEntity = meetBoardRepository.findByIdWithImages(meetBoardId).orElseThrow(()
                    -> new EntityNotFoundException("잘못된 게시글 상세 요청입니다."));
            MeetBoardDetailsResponseDTO meetBoardDetailsResponseDTO = modelMapper.map(meetBoardEntity, MeetBoardDetailsResponseDTO.class);
            meetBoardDetailsResponseDTO.setImages(
                    meetBoardEntity.getMeetBoardImages()
                            .stream()  // 스트림으로 변환
                            .map(MeetBoardImageEntity::getUploadFileUrl)  // MeetBoardImageEntity 에 이미지 URL 추출
                            .collect(Collectors.toList())  // List<String>으로 변환
            );


        } catch (Exception e){

        }

        return null;
    }
}
