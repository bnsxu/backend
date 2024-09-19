package com.example.meettify.repository.member;

import com.example.meettify.entity.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    MemberEntity findByMemberEmail(String memberEmail);
    boolean existsByMemberEmail(String memberEmail);
    boolean existsByNickName(String nickName);
}
