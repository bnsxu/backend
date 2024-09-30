package com.example.meettify.controller.meetBoard;

import com.amazonaws.Response;
import com.example.meettify.dto.meetBoard.*;
import com.example.meettify.service.meetBoard.MeetBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/api/v1/meetBoards")
@Tag(name = "meetBoard", description = "모임 게시판 API")
@RequiredArgsConstructor
public class MeetBoardController {
    private final MeetBoardService meetBoardService;

    @GetMapping("{meetBoardId}")
    @Tag(name = "meetBoard")
    @Operation(summary = "모임 게시물 Detail", description = "모임 게시물 상세 조회 ")
    public ResponseEntity<?> getDetail(@PathVariable Long meetBoardId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            log.info("email : " + email);
            ResponseMeetBoardDetailsDTO meetBoardDetailsResponseDTO = meetBoardService.getDetails(meetBoardId);

            return ResponseEntity.status(HttpStatus.OK).body(meetBoardDetailsResponseDTO);
        } catch (Exception e) {
            log.error("예외 : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //
    @PostMapping("")
    @Tag(name = "meetBoard")
    @Operation(summary = "모임 게시물 등록", description = "모임 게시글 등록하기")
    public ResponseEntity<?> postBoard(@Validated @RequestBody RequestMeetBoardDTO meetBoard, BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails) {

        try {

            if (bindingResult.hasErrors()) {
                log.error("binding error : {}", bindingResult.getAllErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
            }

            MeetBoardServiceDTO meetBoardServiceDTO = MeetBoardServiceDTO.makeServiceDTO(meetBoard);
            ResponseMeetBoardDTO response = meetBoardService.postBoard(meetBoardServiceDTO, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            log.error("예외 : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("{meetId}/{meetBoardId}")
    @Tag(name = "meetBoard")
    @Operation(summary = "모임 게시물 삭제", description = "모임 게시글 삭제하기 ")
    public ResponseEntity<?> deleteBoard(@PathVariable Long meetId, @PathVariable Long meetBoardId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String response = meetBoardService.deleteBoard(meetId, meetBoardId, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("모임 게시글 삭제 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("{meetBoardId}")
    @Tag(name = "meetBoard")
    @Operation(summary = "모임 게시판 수정", description = "모임 게시글 수정하기 ")
    public ResponseEntity<?> updateBoard(@PathVariable Long meetBoardId, UpdateRequestMeetBoardDTO requestMeetBoardDTO, @AuthenticationPrincipal UserDetails userDetails) {
        try {

            UpdateMeetBoardServiceDTO updateMeetBoardServiceDTO = UpdateMeetBoardServiceDTO.makeServiceDTO(requestMeetBoardDTO);
            ResponseMeetBoardDTO response = meetBoardService.updateBoardService(updateMeetBoardServiceDTO, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("모임 게시글 수정 오류 " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
