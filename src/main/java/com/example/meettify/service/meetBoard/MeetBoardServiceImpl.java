package com.example.meettify.service.meetBoard;

import com.example.meettify.dto.meetBoard.MeetBoardDetailsResponseDTO;
import com.example.meettify.entity.meetBoard.MeetBoardEntity;
import com.example.meettify.entity.meetBoard.MeetBoardImageEntity;
import com.example.meettify.exception.meetBoard.MeetBoardException;
import com.example.meettify.repository.meetBoard.MeetBoardImageRepository;
import com.example.meettify.repository.meetBoard.MeetBoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            return meetBoardDetailsResponseDTO;
        } catch (Exception e){
            throw new MeetBoardException(e.getMessage());
        }
    }
}
