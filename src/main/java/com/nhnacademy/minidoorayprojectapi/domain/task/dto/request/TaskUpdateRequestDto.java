package com.nhnacademy.minidoorayprojectapi.domain.task.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class TaskUpdateRequestDto {
    @NotBlank
    private String taskTitle;
    @NotBlank
    private String taskContent;
    @NotBlank
    private String taskStatus;
    @NotNull
    private Long milestoneSeq;
    @NotNull
    private List<Long> tags;


}
