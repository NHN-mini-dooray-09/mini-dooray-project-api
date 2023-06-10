package com.nhnacademy.minidoorayprojectapi.domain.comment.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CommentUpdateRequestDto {
    @NotBlank
    private String commentContent;
}
