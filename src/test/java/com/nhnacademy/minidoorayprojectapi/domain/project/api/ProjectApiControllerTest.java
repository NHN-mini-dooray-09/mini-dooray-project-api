package com.nhnacademy.minidoorayprojectapi.domain.project.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectAuthorityResisterRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectSeqNameDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectApiController.class)
class ProjectApiControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    ProjectService projectService;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetProjectAll() throws Exception {

        List<ProjectSeqNameDto> projects = Arrays.asList(
                new ProjectSeqNameDto(1L, "project1", 1L),
                new ProjectSeqNameDto(2L, "project2", 1L),
                new ProjectSeqNameDto(3L, "project3", 2L)
        );
        Page<ProjectSeqNameDto> projectPage = new PageImpl<>(projects);

        when(projectService.getProjectAll(any(Pageable.class),any()))
                .thenReturn(projectPage);

        mockMvc.perform(get("/projects/all")
                        .param("member-seq","1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content",hasSize(3)))
                .andExpect(jsonPath("$.content[0].projectSeq",equalTo(1)))
                .andExpect(jsonPath("$.content[0].projectName",equalTo("project1")));

        verify(projectService, times(1))
                .getProjectAll(any(),any());
                                                                  }

    @Test
    void testCreateProject() throws Exception {

        ProjectCreateRequestDto projectCreateRequest = ProjectCreateRequestDto.builder()
                .projectName("project1")
                .projectDescription("project-create-test")
                .projectStatus("지연")
                .build();

        when(projectService.createProject(any(),any()))
                .thenReturn(new ProjectSeqDto(1L));

        mockMvc.perform(post("/projects")
                        .param("member-seq","1")
                        .content(objectMapper.writeValueAsString(projectCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.projectSeq",equalTo(1)));

        verify(projectService, times(1))
                .createProject(any(),any());

    }

    @Test
    void testCreateProject_ValidationFailed() throws Exception {
        ProjectCreateRequestDto projectCreateRequest = ProjectCreateRequestDto.builder()
                .projectName("project1")
                .projectDescription("project-create-test")
                .build();

        mockMvc.perform(post("/projects")
                        .param("member-seq","1")
                        .content(objectMapper.writeValueAsString(projectCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(projectService, never())
                .createProject(any(),any());
    }

    @Test
    void getProject() throws Exception {
        ProjectDto projectDto = ProjectDto.builder()
                .projectName("getProject-test")
                .build();

        when(projectService.getProject(any(),any()))
                .thenReturn(projectDto);

        mockMvc.perform(get("/projects/1")
                        .param("member-seq", "1"))
                        .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.projectName",equalTo(projectDto.getProjectName())));

        verify(projectService, times(1))
                .getProject(any(),any());
    }

    @Test
    void updateProject() throws Exception{
        ProjectSeqDto projectSeqDto = new ProjectSeqDto(123L);
        ProjectUpdateRequestDto projectUpdateRequestDto = ProjectUpdateRequestDto
                .builder()
                .projectName("project-update-test")
                .projectDescription("project controller update test")
                .projectStatus("지연")
                .build();

        when(projectService.updateProject(any(),any(),any()))
                .thenReturn(projectSeqDto);

        mockMvc.perform(patch("/projects/1")
                        .param("member-seq","1")
                        .content(objectMapper.writeValueAsString(projectUpdateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectSeq", equalTo(123)));

        verify(projectService, times(1))
                .updateProject(any(),any(),any());
    }

    @Test
    void updateProject_ValidationFailed() throws Exception{

        ProjectUpdateRequestDto projectUpdateRequestDto = ProjectUpdateRequestDto
                .builder()
                .projectName("project-update-test")
                .build();

        mockMvc.perform(patch("/projects/1")
                        .param("member-seq","1")
                        .content(objectMapper.writeValueAsString(projectUpdateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(projectService, never())
                .updateProject(any(),any(),any());
    }

    @Test
    void resisterProjectMember() throws Exception{
        ProjectAuthorityResisterRequestDto projectAuthorityResisterRequestDto = new ProjectAuthorityResisterRequestDto(
                Arrays.asList(1L,2L,3L)
        );

        mockMvc.perform(post("/projects/1/members")
                        .param("member-seq","1")
                        .content(objectMapper.writeValueAsString(projectAuthorityResisterRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(projectService, times(1))
                .resisterProjectMembers(any(),any(),any());
    }

    @Test
    void resisterProjectMember_ValidationFailed() throws Exception{
        mockMvc.perform(post("/projects/1/members")
                        .param("member-seq","1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(projectService, never())
                .resisterProjectMembers(any(),any(),any());
    }
}