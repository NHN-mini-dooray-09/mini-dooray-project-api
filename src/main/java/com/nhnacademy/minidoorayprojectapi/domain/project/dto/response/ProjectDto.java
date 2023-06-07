package com.nhnacademy.minidoorayprojectapi.domain.project.dto.response;

import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.TagSeqNameDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private String projectName;
    private String projectDescription;
    private String projectStatus;
    private LocalDateTime projectCreatedAt;
    private List<TagSeqNameDto> tags;
    private List<ProjectMemberSeqDto> members;

}
