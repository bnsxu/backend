package com.example.meettify.config.auditing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/*
*   worker  : 유요한
*   work    : 등록 시간과 수정 시간을 기록해주는 역할을 하는 클래스
*   date    : 2024/09/19
* */

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@ToString
public class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate
    private LocalDateTime updateTime;
}
