package com.nhnacademy.minidoorayprojectapi.domain.tag.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagCreateRequestDto {
    @NotBlank
    private String tagName;
}
