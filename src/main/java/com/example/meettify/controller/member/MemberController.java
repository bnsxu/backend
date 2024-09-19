package com.example.meettify.controller.member;

import com.example.meettify.dto.member.MemberServiceDTO;
import com.example.meettify.dto.member.RequestMemberDTO;
import com.example.meettify.dto.member.ResponseMemberDTO;
import com.example.meettify.entity.member.MemberEntity;
import com.example.meettify.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/api/v1/members")
@Tag(name= "member", description = "유저 API")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;


    // 회원가입
    @PostMapping("")
    @Tag(name = "member")
    @Operation(summary = "회원가입", description = "회원가입하는 API")
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
    @Tag(name = "member")
    @Operation(summary = "중복체크 API", description = "userEmail이 중복인지 체크하는 API입니다.")
    public boolean emailCheck(@PathVariable String memberEmail) {
        log.info("email : " + memberEmail);
        return memberService.emailCheck(memberEmail);
    }

    // 닉네임 조회
    @GetMapping("/nickName/{nickName}")
    @Tag(name = "member")
    @Operation(summary = "닉네임 조회", description = "중복된 닉네임이 있는지 확인하는 API입니다.")
    public boolean nickNameCheck(@PathVariable String nickName) {
        log.info("nickName : " + nickName);
        return memberService.nickNameCheck(nickName);
    }

}
