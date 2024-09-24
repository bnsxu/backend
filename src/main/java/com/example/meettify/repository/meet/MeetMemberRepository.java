package com.example.meettify.repository.meet;

import com.example.meettify.entity.Meet.MeetMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetMemberRepository extends JpaRepository<MeetMemberEntity, Long> {
}
