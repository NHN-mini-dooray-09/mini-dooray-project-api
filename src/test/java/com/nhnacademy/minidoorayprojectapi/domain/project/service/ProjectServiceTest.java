package com.nhnacademy.minidoorayprojectapi.domain.project.service;

import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectUpdateRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectServiceTest {
    @Autowired
    ProjectService projectService;


    @Test
    void testUpdateProject() {
        ProjectUpdateRequestDto updateRequest = ProjectUpdateRequestDto.builder()
                .projectName("project1-change-test")
                .build();
        projectService.updateProject(1L, updateRequest);
        assertThat(projectService.getProject(1L).getProjectName()).isEqualTo(updateRequest.getProjectName());
    }


}