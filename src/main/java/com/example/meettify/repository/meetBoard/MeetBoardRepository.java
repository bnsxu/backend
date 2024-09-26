package com.example.meettify.repository.meetBoard;

import com.example.meettify.entity.Meet.MeetBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MeetBoardRepository extends JpaRepository<MeetBoardEntity, Long> {

    @Query("SELECT mb FROM meetBoards mb JOIN FETCH mb.meetBoardImages WHERE mb.meetBoardId = :meetBoardId")
    Optional<MeetBoardEntity> findByIdWithImages(@Param("meetBoardId") Long meetBoardId);



    //누가 작성했는지 까지 알아야한다.
    @Query("SELECT mb FROM meetBoards mb JOIN FETCH mb.memberEntity WHERE mb.meetEntity = :meetId")
    Optional<MeetBoardEntity> findMeetBoardEntitiesBy(@Param("meetBoardId") Long meetId);

}