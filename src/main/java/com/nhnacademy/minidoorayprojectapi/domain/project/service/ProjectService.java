package com.nhnacademy.minidoorayprojectapi.domain.project.service;

import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectAuthorityRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectAuthorityResisterRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.*;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.ProjectAuthority;
import com.nhnacademy.minidoorayprojectapi.global.exception.ProjectNotFoundException;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.response.TagSeqNameDto;
import com.nhnacademy.minidoorayprojectapi.global.exception.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectAuthorityRepository projectAuthoritiesRepository;

    /**
     * 전체 프로젝트 목록 : 로그인한 사람것만 가능
     * @param pageable
     * @return
     */
    public Page<ProjectSeqNameDto> getProjectAll(Pageable pageable,Long memberSeq) {
        Page<Project> projectPage = projectRepository.getAllByMemberSeq(pageable, memberSeq);
        return convertToProjectSeqNameDtoPage(projectPage);
    }

    private Page<ProjectSeqNameDto> convertToProjectSeqNameDtoPage(Page<Project> projectPage){
        List<ProjectSeqNameDto> projectPageDto = projectPage.getContent()
                .stream()
                .map(project -> new ProjectSeqNameDto(project.getProjectSeq(),project.getProjectName(),project.getMemberSeq()))
                .collect(Collectors.toList());
        return new PageImpl<>(projectPageDto,projectPage.getPageable(),projectPage.getTotalElements());
    }

    public ProjectDto getProject(Long projectSeq, Long memberSeq){

        Project project = projectRepository.findById(projectSeq)
                .orElseThrow(() -> new ProjectNotFoundException("프로젝트를 찾을 수 없습니다."));

        if(!projectAuthoritiesRepository.existsById(new ProjectAuthority.ProjectAuthoritiesPk(projectSeq,memberSeq))){
            throw new AccessDeniedException();
        }
        return convertToProjectDto(project);
    }

    private ProjectDto convertToProjectDto(Project project) {
        return ProjectDto.builder()
                .projectName(project.getProjectName())
                .projectDescription(project.getProjectDescription())
                .projectStatus(project.getProjectStatus())
                .projectCreatedAt(project.getProjectCreatedAt())
                .tags(new ArrayList<>(project.getTags()
                        .stream()
                        .map(tag -> new TagSeqNameDto(tag.getTagSeq(),tag.getTagName()))
                        .collect(Collectors.toList())))
                .members(new ArrayList<>(project.getProjectMembers()
                        .stream()
                        .map(projectAuthorities -> new ProjectMemberSeqDto(projectAuthorities.getProjectAuthoritiesPk().getMemberSeq()))
                        .collect(Collectors.toList())))
                .build();
    }

    @Transactional
    public ProjectSeqDto createProject(ProjectCreateRequestDto projectCreateRequest,Long memberSeq){
        Project newProject = Project.builder()
                .memberSeq(memberSeq)
                .projectName(projectCreateRequest.getProjectName())
                .projectDescription(projectCreateRequest.getProjectDescription())
                .projectStatus(projectCreateRequest.getProjectStatus())
                .build();
        Project project = projectRepository.save(newProject);
        createProjectAuthorities(memberSeq,project,"PROJECT_ROLE_ADMIN");
        return new ProjectSeqDto(project.getProjectSeq());
    }

    /**
     * valid check 작성자와 수정하는 사람이 같아야 함
     * @param projectSeq
     * @param projectRequest
     * @return projectSeq
     */
    @Transactional
    public ProjectSeqDto updateProject(Long projectSeq, ProjectUpdateRequestDto projectRequest, Long memberSeq) {
        Project project = projectRepository.findById(projectSeq)
                .orElseThrow(()->new ProjectNotFoundException("해당 프로젝트를 찾을 수 없습니다."));

        ProjectAuthority projectAuthority = projectAuthoritiesRepository.findById
                (new ProjectAuthority.ProjectAuthoritiesPk(projectSeq, memberSeq))
                .orElseThrow(AccessDeniedException::new);

        if(!projectAuthority.getAuthority().equals("PROJECT_ROLE_ADMIN")){
            throw new AccessDeniedException();
        }

        project.updateProject(projectRequest.getProjectName(), projectRequest.getProjectStatus(),
                projectRequest.getProjectDescription());


        return new ProjectSeqDto(project.getProjectSeq());
    }


    @Transactional
    public ProjectAuthoritiesMemberSeqDto createProjectAuthorities(Long memberSeq, Project project, String role){
        ProjectAuthority newProjectMember = ProjectAuthority.builder()
                .projectAuthoritiesPk(new ProjectAuthority.ProjectAuthoritiesPk(project.getProjectSeq(), memberSeq))
                .project(project)
                .authority(role)
                .build();

        return new ProjectAuthoritiesMemberSeqDto(projectAuthoritiesRepository.save(newProjectMember)
                .getProjectAuthoritiesPk().getMemberSeq());
    }

    /**
     * 프로젝트 맴버 등록 시 role은 member로 등록된다.
     * @param projectSeq
     * @param memberSeq
     * @param projectAuthoritiesResisterRequest
     */
    @Transactional
    public void resisterProjectMembers(Long projectSeq,
                                       Long memberSeq,
                                       ProjectAuthorityResisterRequestDto projectAuthoritiesResisterRequest){
        Project project = projectRepository.findById(projectSeq)
                .orElseThrow(()->new ProjectNotFoundException("프로젝트를 찾을 수 없습니다."));

        ProjectAuthority projectAuthorities = projectAuthoritiesRepository.findById(new ProjectAuthority.ProjectAuthoritiesPk(projectSeq, memberSeq))
                .orElseThrow(AccessDeniedException::new);

        if(!projectAuthorities.getAuthority().equals("PROJECT_ROLE_ADMIN")){
            throw new AccessDeniedException();
        }

        for (Long member : projectAuthoritiesResisterRequest.getProjectMembers()) {
            createProjectAuthorities(member, project,"PROJECT_ROLE_MEMBER");
        }

    }

}
