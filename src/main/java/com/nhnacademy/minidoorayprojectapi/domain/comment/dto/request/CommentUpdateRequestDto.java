package com.nhnacademy.minidoorayprojectapi.domain.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateRequestDto {
    @NotBlank
    private String commentContent;
}
