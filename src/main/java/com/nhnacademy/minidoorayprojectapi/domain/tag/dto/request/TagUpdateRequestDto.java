package com.nhnacademy.minidoorayprojectapi.domain.tag.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TagUpdateRequestDto {
    @NotBlank
    private String tagName;

}
