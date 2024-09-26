package com.example.meettify.controller.MeetBoard;

import com.example.meettify.dto.meetBoard.MeetBoardDetailsResponseDTO;
import com.example.meettify.service.meetBoard.MeetBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        try{
            String email = userDetails.getUsername();
            log.info("email : " + email);
            MeetBoardDetailsResponseDTO meetBoardDetailsResponseDTO = meetBoardService.getDetails(meetBoardId);

            return ResponseEntity.status(HttpStatus.OK).body(meetBoardDetailsResponseDTO);
        }catch (Exception e) {
            log.error("예외 : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
