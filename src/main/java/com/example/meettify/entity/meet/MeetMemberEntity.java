package com.example.meettify.entity.meet;

import com.example.meettify.config.auditing.entity.BaseEntity;
import com.example.meettify.dto.meet.MeetRole;
import com.example.meettify.entity.member.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/*
 *   worker  : 조영흔
 *   work    : 모임과 멤버 다대다 관계 해결
 *   date    : 2024/09/19
 * */
@Entity(name = "meetMembers")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MeetMemberEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meetMember_id")
    private Long meetMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meet_id")
    private MeetEntity meetEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "meetMember_role", nullable = false)
    private MeetRole meetRole;

    @Column(name = "meetMember_joinDate")
    private LocalDateTime joinDate;
}
