package com.nhnacademy.minidoorayprojectapi.domain.project.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class ProjectCreateRequestDto {
    @NotBlank
    private String projectName;
    private String projectDescription;
    @NotBlank
    private String projectStatus;
}
