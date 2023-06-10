package com.nhnacademy.minidoorayprojectapi.domain.task.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class TaskCreateRequestDto {
    @NotBlank
    private String taskTitle;
    private String taskContent;
    private String taskStatus;
    private Long milestoneSeq;
    private List<Long> tags;
}
