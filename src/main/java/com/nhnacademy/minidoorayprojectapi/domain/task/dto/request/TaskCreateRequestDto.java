package com.nhnacademy.minidoorayprojectapi.domain.task.dto.request;

import lombok.Builder;
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

    @Builder
    public TaskCreateRequestDto(String taskTitle, String taskContent, String taskStatus, Long milestoneSeq, List<Long> tags) {
        this.taskTitle = taskTitle;
        this.taskContent = taskContent;
        this.taskStatus = taskStatus;
        this.milestoneSeq = milestoneSeq;
        this.tags = tags;
    }
}
