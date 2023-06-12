package com.nhnacademy.minidoorayprojectapi.domain.project.service;

import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectSeqDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProjectServiceTest {
    @Autowired
    ProjectService projectService;

//    @Test
//    void testCreateProject() {
//        ProjectCreateRequestDto createRequest = ProjectCreateRequestDto.builder()
//                .projectName("project1-create-test")
//                .projectDescription("project 생성 테스트")
//                .projectStatus("지연")
//                .build();
//        ProjectSeqDto projectSeqDto = projectService.createProject(createRequest, 1L);
//        assertThat(projectService.getProject(projectSeqDto.getProjectSeq(),1L).getProjectName()).isEqualTo(createRequest.getProjectName());
//    }
//
//    @Test
//    void testGetProject() {
////        assertThat(projectService.getProject(1L,1L).getMembers().size()).isEqualTo(3);
//        assertThat(projectService.getProject(1L,1L).getProjectName()).isEqualTo("project1");
//
//    }
//
//
//    @Test
//    void testUpdateProject() {
//        ProjectUpdateRequestDto updateRequest = ProjectUpdateRequestDto.builder()
//                .projectName("project1-change-test")
//                .build();
//        projectService.updateProject(1L, updateRequest,1L);
//        assertThat(projectService.getProject(1L,1L).getProjectName()).isEqualTo(updateRequest.getProjectName());
//    }


}