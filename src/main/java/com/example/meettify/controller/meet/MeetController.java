package com.example.meettify.controller.meet;


import com.example.meettify.dto.meet.*;
import com.example.meettify.dto.meet.category.Category;
import com.example.meettify.dto.meetBoard.MeetBoardSummaryDTO;
import com.example.meettify.service.meet.MeetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/*
 *   worker : 조영흔
 *   work   : 서비스 로직 구현
 *   date   : 2024/09/26
 * */
@RestController
@Log4j2
@RequestMapping("/api/v1/meets")
@Tag(name = "meet", description = "모임 API")
@RequiredArgsConstructor
public class MeetController {
    private final MeetService meetService;

    //모임 리스트
    @GetMapping
    @Tag(name = "meet")
    @Operation(summary = "모임 리스트", description = "모임 데이터 List를 페이징 처리와 함께 제공해주는 기능")
    public ResponseEntity<?> getList(@RequestParam(defaultValue = "0") Long lastId,
                                        @RequestParam(defaultValue = "9") int size
                                        ,@RequestParam(required = false) Category category) {
        try {
            List<MeetSummaryDTO> meetList = meetService.getMeetList(lastId, size,category);
            return ResponseEntity.status(HttpStatus.OK).body(meetList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 모임 리스트 요청입니다");
        }
    }

    //모임 상세 정보
    @GetMapping("{meetId}")
    @Tag(name = "meet")
    @Operation(summary = "모임 디테일 정보", description = "모임 디테일 정보와 현재 모임에서 권한 관련 정보를 전달해줘야 한다.")
    public ResponseEntity<?> getDetail(@PathVariable Long meetId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = (userDetails != null) ? userDetails.getUsername() : null;
            MeetRole meetRole = (email != null) ? meetService.getMeetRole(meetId, email) : MeetRole.EXPEL;  //

            // 모임 디테일 정보를 가져온다.
            MeetDetailDTO meetDetailDTO = meetService.getMeetDetail(meetId);

            List<MeetBoardSummaryDTO> meetBoardSummaryDTO;
            if(meetRole == MeetRole.EXPEL) {
                //회원일 경우에만 게시글 관련 정보를 볼 수 있따.  최근 게시글 3개를 가져온다.
                meetBoardSummaryDTO = new ArrayList<MeetBoardSummaryDTO>();
            }else{
                meetBoardSummaryDTO = meetService.getMeetSummaryList(meetId);
            }

            return ResponseEntity.status(HttpStatus.OK).body(MeetDetailInfoResponseDTO.builder()
                    .meetDetailDTO(meetDetailDTO)
                    .meetBoardSummaryDTOList(meetBoardSummaryDTO)
                    .meetId(meetId)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 모임 조회입니다.");
        }
    }



    @PostMapping("")
    @Tag(name = "meet")
    @Operation(summary = "모임 만들기", description = "모임 만들어 주는 API, 신규 모임정보와 이미지, 회원 정보가 필요하다.")
    // BindingResult 타입의 매개변수를 지정하면 BindingResult 매개 변수가 입력값 검증 예외를 처리한다.
    public ResponseEntity<?> makeMeet(@Valid @RequestBody RequestMeetDTO meet, BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails) {

        try {
            // 입력값 검증 예외가 발생하면 예외 메세지를 출력
            if (bindingResult.hasErrors()) {
                log.error("binding error: {}", bindingResult.getAllErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
            }
            String email = userDetails.getUsername();
            MeetServiceDTO meetServiceDTO = MeetServiceDTO.makeServiceDTO(meet);
            ResponseMeetDTO response = meetService.makeMeet(meetServiceDTO, email);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("예외 : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @DeleteMapping("/{meetId}")
    @Tag(name = "meet")
    @Operation(summary = "삭제 API", description = "소모임 삭제 API")
    public ResponseEntity<?> delete(@PathVariable Long meetId, @AuthenticationPrincipal UserDetails userDetails) {

        try {
            String email = userDetails.getUsername();
            log.info("email : " + email);
            String response = meetService.removeMeet(meetId, email);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{meetId}/members/edit-permission")
    @Tag(name = "meet")
    @Operation(summary = "수정 권한 체크 API", description = "모임 수정 권한을 체크하는 API")
    public ResponseEntity<?> checkEditPermission(@PathVariable Long meetId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            boolean hasPermission = meetService.checkEditPermission(meetId, email); // 수정 권한 체크 로직

            if (hasPermission) {
                return ResponseEntity.ok().body("권한 있음");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한 없음");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{meetId}")
    @Tag(name = "meet")
    @Operation(summary = "수정 API", description = "모임에 대한 수정을 진행하는 API")
    public ResponseEntity<?> updateMeet(@PathVariable Long meetId,
                                        @Validated @RequestBody UpdateMeetDTO updateMeetDTO,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            //권한체크
            boolean hasPermission = meetService.checkEditPermission(meetId, email);
            if (hasPermission) {
                // ServiceDTODTO 바꾸는 로직
                UpdateMeetServiceDTO updateMeetServiceDTO = UpdateMeetServiceDTO.makeServiceDTO(updateMeetDTO);
                // 응답ServiceDTO 받기
                ResponseMeetDTO response = meetService.update(updateMeetServiceDTO, updateMeetDTO.getNewImages());
                //반환하기
                return ResponseEntity.ok().body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한 없음");
    }


    @PostMapping("/{meetId}/members")
    @Operation(summary = "모임 가입 신청", description = "회원이 특정 모임에 가입 신청하는 API")
    public ResponseEntity<?> applyToJoinMeet(@PathVariable Long meetId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // 1. 로그인된 유저인지 확인 (AuthenticationPrincipal로 로그인된 유저 정보 가져옴)
            String userEmail = userDetails.getUsername();

            // 2. 이미 모임에 가입된 회원인지 확인
            boolean alreadyMember = meetService.isAlreadyMember(meetId, userEmail);
            if (alreadyMember) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 가입된 회원입니다.");
            }

            // 3. 회원 가입 신청 처리
            meetService.applyToJoinMeet(meetId, userEmail);
            return ResponseEntity.ok().body("가입 신청이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("오류 발생: " + e.getMessage());
        }
    }
}
