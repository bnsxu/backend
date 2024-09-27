package com.example.meettify.repository.meet;

import com.example.meettify.dto.meet.category.Category;
import com.example.meettify.entity.meet.MeetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface MeetRepository extends JpaRepository<MeetEntity, Long> {

    //모임정보와 이미지 정보를 가져옴
    @Query("SELECT  m FROM meets m JOIN FETCH m.meetImages WHERE m.meetId = :meetId")
    Optional<MeetEntity> findByIdWithImages(@Param("meetId") Long meetId);


    //무한로딩을 위한 정보를 가져옴
    @Query("SELECT m FROM meets m JOIN FETCH m.meetImages WHERE m.meetCategory = :category " +
            "AND m.meetId > :lastId  ORDER BY m.meetId ASC")
    List<MeetEntity> findByMeetIdGreaterThanAndCategory(
            @Param("lastId") Long lastId,
            @Param("category") Category category,
            Pageable pageable);

}
