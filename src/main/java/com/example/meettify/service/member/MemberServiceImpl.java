package com.example.meettify.service.member;

import com.example.meettify.dto.member.MemberServiceDTO;
import com.example.meettify.dto.member.ResponseMemberDTO;
import com.example.meettify.entity.member.MemberEntity;
import com.example.meettify.exception.member.MemberException;
import com.example.meettify.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
*   worker  : 유요한
*   work    : 유저 기능을 하는 서비스 클래스
*   date    : 2024/09/19
* */

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    // 회원가입
    @Override
    public ResponseMemberDTO signUp(MemberServiceDTO member) {
        try {
            String encodePw = passwordEncoder.encode(member.getMemberPw());
            log.info("암호화 비밀번호 : " + encodePw);

            MemberEntity memberEntity = modelMapper.map(member, MemberEntity.class);

            MemberEntity saveMember = memberRepository.save(memberEntity);
            ResponseMemberDTO response = modelMapper.map(saveMember, ResponseMemberDTO.class);
            log.info("response : {}", response);
            return response;
        }catch (Exception e) {
            log.error(e.getMessage());
            throw new MemberException(e.getMessage());
        }
    }

    // 이메일 중복체크
    @Override
    public boolean emailCheck(String email) {
        return !memberRepository.existsByMemberEmail(email);
    }

    // 닉네임 중복체크
    @Override
    public boolean nickNameCheck(String nickName) {
        return !memberRepository.existsByNickName(nickName);
    }
}
