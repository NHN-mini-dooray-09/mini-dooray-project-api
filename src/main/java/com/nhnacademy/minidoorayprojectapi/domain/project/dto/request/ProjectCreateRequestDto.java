package com.nhnacademy.minidoorayprojectapi.domain.project.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ProjectCreateRequestDto {
    @NotNull
    private Long memberSeq;
    @NotBlank
    private String projectName;
    private String projectDescription;
    @NotBlank
    private String projectStatus;
}
