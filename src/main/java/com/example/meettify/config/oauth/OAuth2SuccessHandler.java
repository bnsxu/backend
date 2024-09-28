package com.example.meettify.config.oauth;

import com.example.meettify.config.jwt.JwtProvider;
import com.example.meettify.dto.jwt.TokenDTO;
import com.example.meettify.dto.member.SocialDTO;
import com.example.meettify.dto.member.role.UserRole;
import com.example.meettify.entity.jwt.TokenEntity;
import com.example.meettify.entity.member.MemberEntity;
import com.example.meettify.repository.jwt.TokenRepository;
import com.example.meettify.repository.member.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *   worker : 유요한
 *   work : 소셜 로그인 성공하면 바디에 담아서 JSON으로 보내준다.
 *   date : 2024/09/22
 * */
@Log4j2
@RestControllerAdvice
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            log.info("OAuth2 Login 성공");
            String email = authentication.getName();

            // 유저 조회
            MemberEntity findMember = memberRepository.findByMemberEmail(email);
            // 권한 가져오기
            List<GrantedAuthority> authorities = getAuthorities(findMember);

            // 토큰 생성
            TokenDTO token = jwtProvider.createToken(email, authorities, findMember.getMemberId());
            // 기존에 토큰이 있는지 조회
            TokenEntity findToken = tokenRepository.findByEmail(email);
            TokenEntity saveToken = null;

            // 토큰이 없다면 처음 가입이므로 생성해준다.
            if(findToken == null) {
                TokenEntity tokenEntity = TokenEntity.changeEntity(token);
                saveToken = tokenRepository.save(tokenEntity);
            }

            // 기존에 토근이 존재하면 업데이트
            if(findToken != null) {
                findToken.updateToken(token);
                saveToken = tokenRepository.save(findToken);
            }

            // 소셜 로그인 정보를 반환
            SocialDTO responseMember = SocialDTO.changeDTO(findMember);

            TokenDTO responseToken = TokenDTO.changeDTO(saveToken, token.getAccessToken());

            //  body에 담는다.
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("provider", responseMember.getProvider());
            responseBody.put("providerId", responseMember.getProviderId());
            responseBody.put("accessToken", responseToken.getAccessToken());
            responseBody.put("refreshToken", responseToken.getRefreshToken());
            responseBody.put("member", responseMember);

            // JSON 응답 전송
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(responseBody));


        } catch (Exception e) {
            // 예외가 발생하면 클라이언트에게 오류 반환
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("정보를 찾지 못했습니다.");
            response.getWriter().write("OAuth2.0 성공 후 에러 발생 : " + e.getMessage());
            // 이 메서드는 버퍼에 있는 내용을 클라이언트에게 보낸다.
            // 데이터를 작성하고 나서는 flush()를 호출하여 실제로 데이터를 클라이언트로 전송한다.
            response.getWriter().flush();
        }
    }

    private List<GrantedAuthority> getAuthorities(MemberEntity member) {
        UserRole memberRole = member.getMemberRole();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + memberRole.name()));
        return authorities;
    }
}
