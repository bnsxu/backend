package com.example.meettify.service.meet;

import com.example.meettify.config.s3.S3ImageUploadService;
import com.example.meettify.dto.meet.*;
import com.example.meettify.dto.meet.category.Category;
import com.example.meettify.dto.meetBoard.MeetBoardSummaryDTO;
import com.example.meettify.entity.meet.MeetEntity;
import com.example.meettify.entity.meet.MeetImageEntity;
import com.example.meettify.entity.meet.MeetMemberEntity;
import com.example.meettify.entity.meetBoard.MeetBoardEntity;
import com.example.meettify.entity.member.MemberEntity;
import com.example.meettify.exception.meet.MeetException;
import com.example.meettify.exception.meetBoard.MeetBoardException;
import com.example.meettify.repository.meet.MeetImageRepository;
import com.example.meettify.repository.meet.MeetMemberRepository;
import com.example.meettify.repository.meet.MeetRepository;
import com.example.meettify.repository.meetBoard.MeetBoardRepository;
import com.example.meettify.repository.member.MemberRepository;
import io.jsonwebtoken.io.IOException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
 *   worker : 조영흔
 *   work   : 서비스 로직 구현
 *   date   : 2024/09/24
 * */
@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MeetServiceImpl implements MeetService {
    private final ModelMapper modelMapper;
    private final MeetRepository meetRepository;
    private final MeetImageRepository meetImageRepository;
    private final MeetMemberRepository meetMemberRepository;
    private final MemberRepository memberRepository;
    private final MeetBoardRepository meetBoardRepository;
    private final S3ImageUploadService s3ImageUploadService;  // S3 서비스 추가

    @Override
    public ResponseMeetDTO makeMeet(MeetServiceDTO meet, String email) throws IOException, java.io.IOException {
        // 1. MeetServiceDTO에서 MeetEntity로 변환
        MeetEntity meetEntity = modelMapper.map(meet, MeetEntity.class);

        // 2. MeetEntity를 저장소에 저장
        MeetEntity savedMeet = meetRepository.save(meetEntity);

        // 3. 이미지가 있는 경우, S3에 업로드하고 해당 URL을 MeetImageEntity로 저장
        if (meet.getImagesFile() != null && !meet.getImagesFile().isEmpty()) {
            // S3에 이미지 업로드 및 DTO 생성 (FileDTOFactory 활용)
            List<MeetImageEntity> imageEntities = s3ImageUploadService.upload(
                    "meetImages",
                    meet.getImagesFile(),
                    (oriFileName, uploadFileName, uploadFilePath, uploadFileUrl) -> MeetImageEntity.builder()
                            .meetEntity(savedMeet)
                            .oriFileName(oriFileName)
                            .uploadFileName(uploadFileName)
                            .uploadFilePath(uploadFilePath)
                            .uploadFileUrl(uploadFileUrl)
                            .build()
            );

            // 업로드된 이미지들을 DB에 저장
            meetImageRepository.saveAll(imageEntities);
        }

        // 4. 모임 생성자를 ADMIN으로 MeetMemberEntity에 등록
        MemberEntity creator = memberRepository.findByMemberEmail(email);
        MeetMemberEntity meetMemberEntity = MeetMemberEntity.builder()
                .meetEntity(savedMeet)
                .memberEntity(creator)
                .meetRole(MeetRole.ADMIN)
                .build();

        // MeetMemberEntity 저장
        meetMemberRepository.save(meetMemberEntity);

        // 5. 응답 DTO 생성 및 반환
        ResponseMeetDTO  responseMeetDTO = modelMapper.map(savedMeet, ResponseMeetDTO.class);

        return responseMeetDTO;
    }

    @Override
    public String removeMeet(Long meetId, String email) {
        try {
            MeetMemberEntity meetMemberEntity = meetMemberRepository.findByEmailAndMeetId(email, meetId)
                    .orElseThrow(() -> new MeetException("Member not part of this meet."));
            if (meetMemberEntity.getMeetRole() == MeetRole.ADMIN) {
                meetRepository.deleteById(meetId);
                // S3에 등록된 이미지 있을 경우 삭제되는 로직 작성.
                return "소모임 삭제 완료";
            }
            throw new MeetException("회원ID과 모임 관리자정보가 일치하지 않습니다.");
        } catch (EntityNotFoundException e) {
            throw new MeetException(e.getMessage());
        }
    }

    @Override
    public boolean checkEditPermission(Long meetId, String email) {
        try {
            MeetMemberEntity meetMemberEntity = meetMemberRepository.findByEmailAndMeetId(email, meetId)
                    .orElseThrow(() -> new MeetException("Member not part of this meet."));

            if (meetMemberEntity.getMeetRole() == MeetRole.ADMIN) {
                return true;
            }
        } catch (EntityNotFoundException e) {
            throw new MeetException("권한 관련 정보가 없습니다.");
        }
        return false;
    }

    @Override
    @Transactional
    public ResponseMeetDTO update(UpdateMeetServiceDTO updateMeetServiceDTO, List<MultipartFile> newImages) throws IOException {
        // 1. 변경 요청한 모임이 존재하는지 확인
        MeetEntity findMeet = meetRepository.findById(updateMeetServiceDTO.getMeetId())
                .orElseThrow(() -> new MeetException("변경 대상 엔티티가 존재하지 않습니다."));

        // 2. 업데이트할 모임 정보 적용
        findMeet.updateMeet(updateMeetServiceDTO);

        // 추후에 s3삭제 기능 구현 예정
        // ...

        // 5. 최종적으로 변경된 엔티티 저장
        meetRepository.save(findMeet);

        // 6. 응답 DTO 생성 및 반환
        return modelMapper.map(findMeet, ResponseMeetDTO.class);
    }

    // 이미 가입된 회원인지 확인
    @Override
    public boolean isAlreadyMember(Long meetId, String email) {
        MemberEntity member = memberRepository.findByMemberEmail(email);
        MeetEntity meet = meetRepository.findById(meetId)
                .orElseThrow(() -> new EntityNotFoundException("Meet not found"));
        return meetMemberRepository.existsByMeetEntityAndMemberEntity(meet, member);
    }

    // 가입 신청 처리
    @Override
    @Transactional
    public void applyToJoinMeet(Long meetId, String email) {
        MemberEntity member = memberRepository.findByMemberEmail(email);
        MeetEntity meet = meetRepository.findById(meetId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));

        // MeetMemberEntity에 새로운 가입 요청 저장
        MeetMemberEntity meetMember = MeetMemberEntity.builder()
                .meetEntity(meet)
                .memberEntity(member)
                .meetRole(MeetRole.MEMBER) // 일반 회원으로 가입
                .build();

        meetMemberRepository.save(meetMember);
    }

    @Override
    public MeetDetailDTO getMeetDetail(Long meetId) {
        try {
            // 1. meetId로 MeetEntity 조회
            MeetEntity meetEntity = meetRepository.findByIdWithImages(meetId)
                    .orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않음."));

            // 2. MeetEntity를 MeetDetailDTO로 매핑
            MeetDetailDTO responseMeetDTO = modelMapper.map(meetEntity, MeetDetailDTO.class);

            return responseMeetDTO;
        }catch (Exception e){
            throw new MeetException(e.getMessage());
        }
    }

    @Override
    public List<MeetSummaryDTO> getMeetList(Long lastId, int size, Category category) {
        Pageable pageable = PageRequest.of(0, size, Sort.by("meetId").ascending());
        List<MeetEntity> meets = meetRepository.findByMeetIdGreaterThanAndCategory(lastId, category, pageable);

        return meets.stream()
                .map(meet -> MeetSummaryDTO.builder()
                        .meetId(meet.getMeetId())
                        .meetName(meet.getMeetName())
                        .location(meet.getMeetLocation())
                        .category(meet.getMeetCategory())
                        .maximum(meet.getMeetMaximum())
                        .imageUrls(meet.getMeetImages().stream() // List<MeetImageEntity>를 List<String>으로 변환
                                .map(MeetImageEntity::getUploadFileUrl) // MeetImageEntity의 getImageUrl() 메서드를 호출하여 URL 가져오기
                                .collect(Collectors.toList())) // List<String>으로 수집
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public MeetRole getMeetRole(Long meetId, String email) {
        try {

            MeetMemberEntity meetMemberEntity = meetMemberRepository.findByEmailAndMeetId(email, meetId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

            return meetMemberEntity.getMeetRole();
        } catch (Exception e) {
            throw new MeetException(e.getMessage());
        }
    }

    @Override
    public List<MeetBoardSummaryDTO>  getMeetSummaryList(Long meetId) {
        try {
            Pageable pageable = PageRequest.of(0, 3);  // 첫 번째 페이지에서 3개의 결과만 가져옴
            List<MeetBoardEntity> recentMeetBoards = meetBoardRepository.findTop3MeetBoardEntitiesByMeetId(meetId, pageable);

            // 게시글이 없을 경우
            if (recentMeetBoards.isEmpty()) {
                return Collections.emptyList();
            }

            return recentMeetBoards.stream().map(board -> MeetBoardSummaryDTO.builder()
                    .meetBoardId(board.getMeetBoardId())
                    .title(board.getMeetBoardTitle())
                    .postDate(board.getPostDate())
                    .nickName(board.getMemberEntity().getNickName())
                    .build()).toList();
        }catch (DataAccessException dae) {
            throw new MeetBoardException("데이터베이스 접근 중 오류가 발생했습니다.");
        } catch (NullPointerException npe) {
            throw new MeetBoardException("필요한 데이터가 누락되었습니다.");
        } catch (Exception e) {
            throw new MeetBoardException("알 수 없는 오류가 발생했습니다.");
        }

    }

}
