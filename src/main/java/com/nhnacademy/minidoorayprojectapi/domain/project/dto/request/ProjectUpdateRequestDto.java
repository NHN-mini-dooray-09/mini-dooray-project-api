package com.nhnacademy.minidoorayprojectapi.domain.project.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class ProjectUpdateRequestDto {
    @NotBlank
    private String projectName;
    @NotBlank
    private String projectDescription;
    @NotBlank
    private String projectStatus;
}
