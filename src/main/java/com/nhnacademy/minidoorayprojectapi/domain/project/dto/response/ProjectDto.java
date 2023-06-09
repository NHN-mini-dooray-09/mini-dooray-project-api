package com.nhnacademy.minidoorayprojectapi.domain.project.dto.response;

import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.response.TagSeqNameDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectDto {
    private String projectName;
    private String projectDescription;
    private String projectStatus;
    private LocalDateTime projectCreatedAt;
    private List<TagSeqNameDto> tags;
    private List<ProjectMemberSeqDto> members;

    @Builder
    public ProjectDto(String projectName, String projectDescription, String projectStatus, LocalDateTime projectCreatedAt, List<TagSeqNameDto> tags, List<ProjectMemberSeqDto> members) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectStatus = projectStatus;
        this.projectCreatedAt = projectCreatedAt;
        this.tags = tags;
        this.members = members;
    }
}
