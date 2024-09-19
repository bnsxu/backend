package com.example.meettify.service.member;

import com.example.meettify.dto.member.MemberServiceDTO;
import com.example.meettify.dto.member.RequestMemberDTO;
import com.example.meettify.dto.member.ResponseMemberDTO;
import com.example.meettify.entity.member.MemberEntity;

public interface MemberService {
    // 회원가입
    ResponseMemberDTO signUp(MemberServiceDTO member);
    // 이메일 중복 체크
    boolean emailCheck(String email);
    // 닉네임 중복 체크
    boolean nickNameCheck(String nickName);
}
