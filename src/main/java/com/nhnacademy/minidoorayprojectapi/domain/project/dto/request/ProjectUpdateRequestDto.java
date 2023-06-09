package com.nhnacademy.minidoorayprojectapi.domain.project.dto.request;

import com.nhnacademy.minidoorayprojectapi.domain.tag.entity.Tag;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.Task;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
public class ProjectUpdateRequestDto {
    @NotBlank
    private String projectName;
    @NotBlank
    private String projectDescription;
    @NotBlank
    private String projectStatus;
//    @NotNull
//    private List<Task> tasks;
//    @NotNull
//    private List<Tag> tags;
//    @NotNull
//    private List<Long> milestones;
//    @NotNull
//    private List<Long> projectMembers;
}
