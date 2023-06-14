package com.nhnacademy.minidoorayprojectapi.domain.tag.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagUpdateRequestDto {
    @NotBlank
    private String tagName;

}
