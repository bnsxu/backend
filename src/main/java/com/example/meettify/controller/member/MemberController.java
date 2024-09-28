package com.example.meettify.controller.member;

import com.example.meettify.dto.jwt.TokenDTO;
import com.example.meettify.dto.member.*;
import com.example.meettify.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController implements MemberControllerDocs{
    private final MemberService memberService;


    // 회원가입
    @PostMapping("")
    // BindingResult 타입의 매개변수를 지정하면 BindingResult 매개 변수가 입력값 검증 예외를 처리한다.
    public ResponseEntity<?> signUp(@Validated @RequestBody RequestMemberDTO member,
                                    BindingResult bindingResult) {
        try {
            // 입력값 검증 예외가 발생하면 예외 메시지를 출력
            if (bindingResult.hasErrors()) {
                log.error("binding error: {}", bindingResult.getAllErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
            }

            MemberServiceDTO memberServiceDTO = MemberServiceDTO.makeServiceDTO(member);
            ResponseMemberDTO response = memberService.signUp(memberServiceDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("예외 : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 중복체크
    @GetMapping("/email/{memberEmail}")
    public boolean emailCheck(@PathVariable String memberEmail) {
        log.info("email : " + memberEmail);
        return memberService.emailCheck(memberEmail);
    }

    // 닉네임 조회
    @GetMapping("/nickName/{nickName}")
    public boolean nickNameCheck(@PathVariable String nickName) {
        log.info("nickName : " + nickName);
        return memberService.nickNameCheck(nickName);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        try {
            String email = login.getEmail();
            String memberPw = login.getMemberPw();
            TokenDTO response = memberService.login(email, memberPw);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 회원 수정
    @PutMapping("/{memberId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long memberId,
                                    @Validated @RequestBody UpdateMemberDTO updateMemberDTO,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            log.info("email : " + email);
            UpdateMemberServiceDTO updateMemberServiceDTO = UpdateMemberServiceDTO.makeServiceDTO(updateMemberDTO);
            ResponseMemberDTO reponse = memberService.update(updateMemberServiceDTO, email);
            return ResponseEntity.ok().body(reponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/{memberId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long memberId,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            log.info("email : " + email);
            String response = memberService.removeUser(memberId, email);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
