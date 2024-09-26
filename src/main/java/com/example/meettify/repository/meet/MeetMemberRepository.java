package com.example.meettify.repository.meet;

import com.example.meettify.entity.Meet.MeetMemberEntity;
import com.example.meettify.entity.Meet.MeetEntity;
import com.example.meettify.entity.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MeetMemberRepository extends JpaRepository<MeetMemberEntity, Long> {

    @Query("SELECT mm FROM meetMembers mm " +
            "JOIN FETCH mm.memberEntity m " +
            "JOIN FETCH mm.meetEntity me " +
            "WHERE m.memberEmail = :email AND me.meetId = :meetId")
    Optional<MeetMemberEntity> findByEmailAndMeetId(@Param("email") String email, @Param("meetId") Long meetId);

    boolean existsByMeetEntityAndMemberEntity(MeetEntity meetEntity, MemberEntity memberEntity);
}
