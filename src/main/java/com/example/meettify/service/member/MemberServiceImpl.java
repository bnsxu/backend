package com.example.meettify.service.member;

import com.example.meettify.config.jwt.JwtProvider;
import com.example.meettify.dto.jwt.TokenDTO;
import com.example.meettify.dto.member.MemberServiceDTO;
import com.example.meettify.dto.member.MemberUpdateServiceDTO;
import com.example.meettify.dto.member.ResponseMemberDTO;
import com.example.meettify.dto.member.role.UserRole;
import com.example.meettify.entity.jwt.TokenEntity;
import com.example.meettify.entity.member.AddressEntity;
import com.example.meettify.entity.member.MemberEntity;
import com.example.meettify.exception.member.MemberException;
import com.example.meettify.repository.jwt.TokenRepository;
import com.example.meettify.repository.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/*
 *   worker  : 유요한
 *   work    : 유저 기능을 하는 서비스 클래스
 *   date    : 2024/09/19
 * */

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

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
        } catch (Exception e) {
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

    @Override
    public TokenDTO login(String email, String password) {
        try {
            boolean isMember = memberRepository.existsByMemberEmail(email);
            TokenEntity findToken = null;
            TokenDTO token = null;
            if (isMember) {
                MemberEntity findMember = memberRepository.findByMemberEmail(email);
                // DB에 넣어져 있는 비밀번호는 암호화가 되어 있어서 비교하는 기능을 사용해야 합니다.
                // 사용자가 입력한 패스워드를 암호화하여 사용자 정보와 비교
                if (passwordEncoder.matches(password, findMember.getMemberPw())) {
                    List<GrantedAuthority> authorities = getAuthorities(findMember);
                    token = jwtProvider.createToken(email, authorities, findMember.getMemberId());
                    findToken = tokenRepository.findByEmail(email);
                } else {
                    findToken.updateToken(token);
                }
                TokenEntity saveToken = tokenRepository.save(findToken);
                TokenDTO response = modelMapper.map(saveToken, TokenDTO.class);
                log.info("response : {}", response);
                return response;
            }
            throw new EntityNotFoundException("회원이 존재하지 않습니다.");
        } catch (Exception e) {
            throw new MemberException(e.getMessage());
        }
    }

    // 회원의 권한을 GrantedAuthority 타입으로 반환하는 메서드
    private List<GrantedAuthority> getAuthorities(MemberEntity member) {
        UserRole memberRole = member.getMemberRole();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + memberRole.name()));
        return authorities;
    }

    // 회원 수정
    @Override
    public ResponseMemberDTO update(MemberUpdateServiceDTO updateServiceDTO,
                                    String email) {
        try {
            boolean isEmail = memberRepository.existsByMemberEmail(email);
            String encodePw = null;
            MemberEntity findMember;

            if (isEmail) {
                findMember = memberRepository.findByMemberEmail(email);
                if (updateServiceDTO.getMemberPw() != null) {
                    encodePw = passwordEncoder.encode(updateServiceDTO.getMemberPw());
                }

                findMember.updateMember(updateServiceDTO, encodePw);
                MemberEntity update = memberRepository.save(findMember);
                ResponseMemberDTO response = modelMapper.map(update, ResponseMemberDTO.class);
                log.info("response : {}", response);
                return response;
            }
            throw new EntityNotFoundException("회원이 존재하지 않습니다.");
        } catch (Exception e) {
            throw new MemberException(e.getMessage());
        }
    }
}
