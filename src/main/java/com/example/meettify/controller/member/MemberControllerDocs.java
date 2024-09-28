package com.example.meettify.controller.member;

import com.example.meettify.dto.member.LoginDTO;
import com.example.meettify.dto.member.RequestMemberDTO;
import com.example.meettify.dto.member.UpdateMemberDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@Tag(name = "member", description = "유저 API")
public interface MemberControllerDocs {
    @Operation(summary = "회원가입", description = "회원가입하는 API")
    ResponseEntity<?> signUp(@Validated RequestMemberDTO member, BindingResult bindingResult);

    @Operation(summary = "중복체크 API", description = "userEmail이 중복인지 체크하는 API")
    boolean emailCheck(String memberEmail);

    @Operation(summary = "닉네임 조회", description = "중복된 닉네임이 있는지 확인하는 API")
    boolean nickNameCheck(String nickName);

    @Operation(summary = "로그인 API", description = "로그인하면 JWT 발급하는 API")
    ResponseEntity<?> login(LoginDTO login);

    @Operation(summary = "수정 API", description = "유저 정보 수정 API")
    ResponseEntity<?> update(Long memberId, @Valid UpdateMemberDTO updateMemberDTO, UserDetails userDetails);

    @Operation(summary = "탈퇴 API", description = "회원 탈퇴 API")
    ResponseEntity<?> delete(Long memberId, UserDetails userDetails);
}
