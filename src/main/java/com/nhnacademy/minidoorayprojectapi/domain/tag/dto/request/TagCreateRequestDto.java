package com.nhnacademy.minidoorayprojectapi.domain.tag.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TagCreateRequestDto {
    @NotBlank
    private String tagName;
}
