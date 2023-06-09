package com.nhnacademy.minidoorayprojectapi.domain.project.service;

import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectAuthoritiesRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectAuthoritiesResisterRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.*;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.ProjectAuthorities;
import com.nhnacademy.minidoorayprojectapi.domain.project.exception.ProjectPermissionDeniedException;
import com.nhnacademy.minidoorayprojectapi.domain.project.exception.ProjectNotFoundException;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.response.TagSeqNameDto;
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
    private final ProjectAuthoritiesRepository projectAuthoritiesRepository;

    /**
     * 전체 프로젝트 목록
     * @param pageable
     * @return
     */
    public Page<ProjectSeqNameDto> getProjectAll(Pageable pageable) {
        Page<Project> projectPage = projectRepository.getAllBy(pageable);
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
                .orElseThrow(() -> new ProjectNotFoundException());
        if(project.getMemberSeq() != memberSeq){
            throw new ProjectPermissionDeniedException();
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
                .orElseThrow(()->new ProjectNotFoundException());
        if(project.getMemberSeq() != memberSeq){
            throw new ProjectPermissionDeniedException();
        }

        project.updateProject(projectRequest.getProjectName(), projectRequest.getProjectStatus(), project.getProjectDescription());


        return new ProjectSeqDto(projectRepository.save(project).getProjectSeq());
    }


    @Transactional
    public ProjectAuthoritiesMemberSeqDto createProjectAuthorities(Long memberSeq, Project project, String role){
        ProjectAuthorities newProjectMember = ProjectAuthorities.builder()
                .projectAuthoritiesPk(new ProjectAuthorities.ProjectAuthoritiesPk(project.getProjectSeq(), memberSeq))
                .project(project)
                .projectAuthority(role)
                .build();
        return new ProjectAuthoritiesMemberSeqDto(projectAuthoritiesRepository.save(newProjectMember).getProjectAuthoritiesPk().getMemberSeq());
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
                                       ProjectAuthoritiesResisterRequestDto projectAuthoritiesResisterRequest){
        Project project = projectRepository.findById(projectSeq)
                .orElseThrow(ProjectNotFoundException::new);

        if(!getProjectAuthorities(projectSeq, memberSeq).getProjectAuthority().equals("PROJECT_ROLE_ADMIN")){
            throw new ProjectPermissionDeniedException();
        }

        for (Long member : projectAuthoritiesResisterRequest.getProjectMembers()) {
            createProjectAuthorities(member, project,"PROJECT_ROLE_MEMBER");
        }

    }

    public ProjectAuthoritiesDto getProjectAuthorities(Long projectSeq, Long memberSeq) {
        ProjectAuthorities projectAuthorities = projectAuthoritiesRepository.findById(new ProjectAuthorities.ProjectAuthoritiesPk(projectSeq, memberSeq))
                .orElseThrow(ProjectPermissionDeniedException::new);

        return convertToProjectAuthoritiesDto(projectAuthorities);
    }

    private ProjectAuthoritiesDto convertToProjectAuthoritiesDto(ProjectAuthorities projectAuthorities){
        return ProjectAuthoritiesDto.builder()
                .projectSeq(projectAuthorities.getProjectAuthoritiesPk().getProjectSeq())
                .memberSeq(projectAuthorities.getProjectAuthoritiesPk().getMemberSeq())
                .projectAuthority(projectAuthorities.getProjectAuthority())
                .build();
    }

}
