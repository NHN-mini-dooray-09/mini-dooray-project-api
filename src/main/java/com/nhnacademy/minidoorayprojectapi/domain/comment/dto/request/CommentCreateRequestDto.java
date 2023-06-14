package com.nhnacademy.minidoorayprojectapi.domain.comment.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class CommentCreateRequestDto {
    @NotNull
    private Long taskSeq;
    @NotBlank
    private String commentContent;
    @NotNull
    private Long memberSeq;

    @Builder
    public CommentCreateRequestDto(Long taskSeq, String commentContent, Long memberSeq) {
        this.taskSeq = taskSeq;
        this.commentContent = commentContent;
        this.memberSeq = memberSeq;
    }
}
