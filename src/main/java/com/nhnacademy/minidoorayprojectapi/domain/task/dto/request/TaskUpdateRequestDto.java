package com.nhnacademy.minidoorayprojectapi.domain.task.dto.request;

import lombok.Builder;
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

    @Builder
    public TaskUpdateRequestDto(String taskTitle, String taskContent, String taskStatus, Long milestoneSeq, List<Long> tags) {
        this.taskTitle = taskTitle;
        this.taskContent = taskContent;
        this.taskStatus = taskStatus;
        this.milestoneSeq = milestoneSeq;
        this.tags = tags;
    }
}
