package com.nhnacademy.minidoorayprojectapi.domain.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long commentSeq;
    private Long memberSeq;
    private String commentContent;
    private LocalDateTime commentCreatedAt;
}
