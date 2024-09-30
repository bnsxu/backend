package com.example.meettify.service.meetBoard;

import com.example.meettify.config.s3.S3ImageUploadService;
import com.example.meettify.dto.meet.MeetRole;
import com.example.meettify.dto.meetBoard.MeetBoardServiceDTO;
import com.example.meettify.dto.meetBoard.ResponseMeetBoardDetailsDTO;
import com.example.meettify.dto.meetBoard.ResponseMeetBoardDTO;
import com.example.meettify.entity.meet.MeetMemberEntity;
import com.example.meettify.entity.meetBoard.MeetBoardEntity;
import com.example.meettify.entity.meetBoard.MeetBoardImageEntity;
import com.example.meettify.entity.member.MemberEntity;
import com.example.meettify.exception.meetBoard.MeetBoardException;
import com.example.meettify.repository.meet.MeetMemberRepository;
import com.example.meettify.repository.meet.MeetRepository;
import com.example.meettify.repository.meetBoard.MeetBoardImageRepository;
import com.example.meettify.repository.meetBoard.MeetBoardRepository;
import com.example.meettify.repository.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MeetBoardServiceImpl implements MeetBoardService {
    private final MeetBoardRepository meetBoardRepository;
    private final MeetBoardImageRepository meetBoardImageRepository;
    private final MeetRepository meetRepository;
    private final MeetMemberRepository meetMemberRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final S3ImageUploadService s3ImageUploadService;  // S3 서비스 추가



    @Override
    public ResponseMeetBoardDetailsDTO getDetails(Long meetBoardId) {
        try {
            MeetBoardEntity meetBoardEntity = meetBoardRepository.findByIdWithImages(meetBoardId).orElseThrow(()
                    -> new EntityNotFoundException("잘못된 게시글 상세 요청입니다."));
            ResponseMeetBoardDetailsDTO meetBoardDetailsResponseDTO = modelMapper.map(meetBoardEntity, ResponseMeetBoardDetailsDTO.class);
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

    @Override
    public ResponseMeetBoardDTO postBoard(MeetBoardServiceDTO meetBoardServiceDTO, String email) throws Exception {
        try {
            // 1. 이메일로 작성자 정보 조회
            MemberEntity member = memberRepository.findByMemberEmail(email);
            long memberId = member.getMemberId();
            long meetId = meetBoardServiceDTO.getMeetId();

            // 2. 모임에 포스팅할 수 있는 권한이 있는지 확인
            MeetMemberEntity meetMemberEntity = meetMemberRepository.findByEmailAndMeetId(email, meetId)
                    .orElseThrow(() -> new EntityNotFoundException("모임에 접근 권한이 없습니다"));

            // 3. meetBoardEntity 변환 및 작성자와 모임 정보 설정
            MeetBoardEntity meetBoard =  MeetBoardEntity.postMeetBoard(meetBoardServiceDTO, member, meetMemberEntity.getMeetEntity());

            // 4. meetBoardEntity 저장
            MeetBoardEntity savedMeetBoard = meetBoardRepository.save(meetBoard);

            // 5. 이미지가 있을 경우 이미지 리스트 저장
            if (meetBoardServiceDTO.getImagesFile() != null && !meetBoardServiceDTO.getImagesFile().isEmpty()) {
                List<MeetBoardImageEntity> imageEntities = s3ImageUploadService.upload(
                        "meetImages",
                        meetBoardServiceDTO.getImagesFile(),
                        (oriFileName, uploadFileName, uploadFilePath, uploadFileUrl) -> MeetBoardImageEntity.builder()
                                .meetBoardEntity(savedMeetBoard)
                                .oriFileName(oriFileName)
                                .uploadFileName(uploadFileName)
                                .uploadFilePath(uploadFilePath)
                                .uploadFileUrl(uploadFileUrl)
                                .build()
                );

                // 6. 업로드된 이미지들을 DB에 저장 및 meetBoard에 추가
                meetBoardImageRepository.saveAll(imageEntities);
                imageEntities.forEach(savedMeetBoard::addMeetBoardImage);
            }

            // 7. 응답 DTO 생성 및 반환
            return ResponseMeetBoardDTO.changeDTO(savedMeetBoard);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String deleteBoard(Long meetId, Long meetBoardId, String email) throws Exception {
        try {
            MeetMemberEntity meetMember = meetMemberRepository.findMeetMemberWithBoardAndMember(email, meetId, meetBoardId)
                    .orElseThrow(EntityNotFoundException::new);

            MeetBoardEntity meetBoard = meetMember.getMeetEntity().getMeetBoardEntity().stream()
                    .filter(board -> board.getMeetBoardId().equals(meetBoardId))
                    .findFirst()
                    .orElseThrow(EntityNotFoundException::new);

            if (meetMember.getMemberEntity().equals(meetBoard.getMemberEntity()) ||
                    meetMember.getMeetRole() == MeetRole.ADMIN) {

                //S3에서 이미지 삭제하는 로직 만들기.

                // 이미지 경로 가져와서 삭제 (이미지 삭제 로직은 그대로 유지)
                meetBoardRepository.delete(meetBoard);

                return "게시물을 삭제했습니다.";
            }
            return "게시물을 삭제할 권한이 없습니다.";
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


}
