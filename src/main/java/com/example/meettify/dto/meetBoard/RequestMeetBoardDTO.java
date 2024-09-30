package com.example.meettify.dto.meetBoard;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/*
 *   worker  : 조영흔
 *   work    : 모임 게시판 생성시 프론트가 서버로 보내준는 request
 *   date    : 2024/09/29
 * */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class RequestMeetBoardDTO {

    @Schema(description = "어떤 모임에 대한 게시글인지", example = "3")
    @NotNull(message = "어떤 모임에 포스팅하는지는 필수입니다..")
    private Long meetId;

    @Schema(description = "게시판 제목", example = "운동 모임 정말 즐거웠던 후기")
    @NotNull(message = "게시글 제목은 필수입니다.")
    @Length(min = 1, max = 80)
    private String meetBoardTitle;

    @Schema(description = "게시글 내용", example = "우리는 즐거운 모임을 했고 정말 행복했다.")
    @NotNull(message = "게시판 내용은 필수입니다.")
    @Pattern(regexp="^[\\S\\s]{1,2500}$", message = "컨텐츠은 1자 이상 2500자 이하이어야 합니다.")
    private String meetBoardContent;


    @Schema(description = "모임 게시판 이미지 리스트", example = "main.jpg")
    @Null
    private List<MultipartFile> images;

    @Schema(description = "게시판 작성 시간", example = "")
    @Null
    private LocalDateTime postDate;


}
