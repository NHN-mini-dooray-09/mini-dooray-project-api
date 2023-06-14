package com.nhnacademy.minidoorayprojectapi.domain.project.service;

import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectAuthorityRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectAuthorityResisterRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectAuthoritiesMemberSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectSeqNameDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.ProjectAuthority;
import com.nhnacademy.minidoorayprojectapi.global.exception.ProjectNotFoundException;
import com.nhnacademy.minidoorayprojectapi.global.exception.AccessDeniedException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProjectServiceTest {
    @InjectMocks
    ProjectService projectService;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    ProjectAuthorityRepository projectAuthorityRepository;

    @Test
    @Order(1)
    @DisplayName("사용자의 모든 프로젝트- 페이지")
    void testGetProjectAll(){

        List<Project> contentList = new ArrayList<>();
        contentList.add(mock(Project.class));
        contentList.add(mock(Project.class));
        contentList.add(mock(Project.class));

        Pageable pageable = PageRequest.of(0,3);

        Page<Project> projectPage = new PageImpl<>(contentList, pageable, contentList.size());

        when(projectRepository.getAllByMemberSeq(pageable,1L))
                .thenReturn(projectPage);

        Page<ProjectSeqNameDto> expected = projectService.getProjectAll(pageable,1L);

        assertThat(expected.getContent()).hasSize(3);
    }


    @Test
    @Order(2)
    @DisplayName("특정 프로젝트")
    void testGetProject() {
        when(projectRepository.findById(any()))
                .thenReturn(Optional.of(mock(Project.class)));

        when(projectAuthorityRepository.existsById(any()))
                .thenReturn(true);

        ProjectDto actual = projectService.getProject(1L, 2L);

        assertThat(actual).isInstanceOf(ProjectDto.class);

    }

    @Test
    @Order(3)
    @DisplayName("project 생성")
    void testCreateProject() {
        ProjectCreateRequestDto createRequest = ProjectCreateRequestDto.builder().build();
        Project project = Project.builder().build();
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectAuthority newProjectMember = ProjectAuthority.builder()
                .projectAuthoritiesPk(new ProjectAuthority.ProjectAuthoritiesPk(project.getProjectSeq(),1L))
                .build();
        when(projectAuthorityRepository.save(any(ProjectAuthority.class))).thenReturn(newProjectMember);

        ProjectSeqDto expected = projectService.createProject(createRequest, 1L);

        assertThat(expected.getProjectSeq()).isEqualTo(project.getProjectSeq());
    }


    @Test
    void testUpdateProject() {
        Project project = Project.builder()
                .memberSeq(1L)
                .build();

        ProjectAuthority projectAuthority = ProjectAuthority.builder()
                .authority("PROJECT_ROLE_ADMIN")
                .build();

        ProjectUpdateRequestDto projectUpdateRequest = ProjectUpdateRequestDto.builder()
                .projectName("project-service-update-test")
                .build();

        when(projectRepository.findById(any())).thenReturn(Optional.ofNullable(project));
        when(projectAuthorityRepository.findById(any())).thenReturn(Optional.ofNullable(projectAuthority));

        ProjectSeqDto expected = projectService.updateProject(project.getProjectSeq(),
                projectUpdateRequest,1L );

        verify(projectRepository, times(1))
                .findById(any());

        assertThat(expected.getProjectSeq()).isEqualTo(project.getProjectSeq());
        assertThat(project.getProjectName()).isEqualTo(projectUpdateRequest.getProjectName());


    }

    @Test
    @DisplayName("프로젝트 맴버 등록")
    void testResisterProjectMembers(){
        Project project = Project.builder().build();

        ProjectAuthority projectAuthority = ProjectAuthority.builder()
                .authority("PROJECT_ROLE_ADMIN")
                .build();
        when(projectRepository.findById(any())).thenReturn(Optional.ofNullable(project));
        when(projectAuthorityRepository.findById(any()))
                .thenReturn(Optional.ofNullable(projectAuthority));

        when(projectAuthorityRepository.save(any()))
                .thenReturn(mock(ProjectAuthority.class));

        ProjectAuthorityResisterRequestDto projectAuthorityResisterRequest =
                new ProjectAuthorityResisterRequestDto(Arrays.asList(1L, 2L));

        when(projectAuthorityRepository.save(any(ProjectAuthority.class)))
                .thenAnswer(invocation -> {
                    ProjectAuthority saveProjectAuthority = invocation.getArgument(0);
                    return saveProjectAuthority;
                });

        projectService.resisterProjectMembers(project.getProjectSeq(), any(),projectAuthorityResisterRequest);

        verify(projectAuthorityRepository, times(projectAuthorityResisterRequest.getProjectMembers().size()))
                .save(any(ProjectAuthority.class));
    }

    @Test
    @DisplayName("프로젝트 멤버 및 권한 추가")
    void testCreateProjectAuthority(){
        Project project = Project.builder().build();

        ProjectAuthority projectAuthority = ProjectAuthority.builder()
                .projectAuthoritiesPk(new ProjectAuthority.ProjectAuthoritiesPk(project.getProjectSeq(),123L))
                .project(project)
                .build();

        when(projectAuthorityRepository.save(any()))
                .thenReturn(projectAuthority);

        ProjectAuthoritiesMemberSeqDto result =
                projectService.createProjectAuthorities(any(),project,"PROJECT_ROLE_MEMBER");

        assertNotNull(result);
        assertEquals(result.getMemberSeq(),123L);
    }


    @Test
    @DisplayName("getProject할 때 ProjectNotFoundException")
    void testGetProjectNotFoundException() {
        when(projectRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class,
                () -> projectService.getProject(1L, 2L));
    }

    @Test
    @DisplayName("getProject할 때 UnauthorizedAccessException")
    void testGetProjectUnauthorizedException() {
        when(projectRepository.findById(any()))
                .thenReturn(Optional.of(mock(Project.class)));

        when(projectAuthorityRepository.existsById(any()))
                .thenReturn(false);

        assertThrows(AccessDeniedException.class,
                () -> projectService.getProject(1L, 2L));
    }

    @Test
    @DisplayName("updateProject할 때 UnauthorizedAccessException")
    void testUpdateProjectUnauthorizedAccessException(){
        Project project = Project.builder()
                .memberSeq(1L)
                .build();

        ProjectAuthority projectAuthority = ProjectAuthority.builder()
                .authority("PROJECT_ROLE_MEMBER")
                .build();


        when(projectRepository.findById(any())).thenReturn(Optional.ofNullable(project));
        when(projectAuthorityRepository.findById(any())).thenReturn(Optional.ofNullable(projectAuthority));

        assertThrows(AccessDeniedException.class,
                () -> projectService.updateProject(project.getProjectSeq(), mock(ProjectUpdateRequestDto.class),any()));

    }

    @Test
    @DisplayName("resisterProjectMembers admin권한이 아니면  UnauthorizedAccessException")
    void testResisterProjectMembersUnauthorizedAccessException(){
        Project project = Project.builder().build();

        ProjectAuthority projectAuthority = ProjectAuthority.builder()
                .authority("PROJECT_ROLE_MEMBER")
                .build();

        when(projectRepository.findById(any())).thenReturn(Optional.ofNullable(project));
        when(projectAuthorityRepository.findById(any()))
                .thenReturn(Optional.ofNullable(projectAuthority));

        ProjectAuthorityResisterRequestDto projectAuthorityResisterRequest =
                new ProjectAuthorityResisterRequestDto(Arrays.asList(1L, 2L));

        assertThrows(AccessDeniedException.class,
                () -> projectService.resisterProjectMembers(project.getProjectSeq(),any(), projectAuthorityResisterRequest));
    }

}