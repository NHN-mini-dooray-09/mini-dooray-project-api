package com.nhnacademy.minidoorayprojectapi.domain.project.service;

import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectMemberSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectSeqNameDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.domain.project.exception.ProjectNotFoundException;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.TagSeqNameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;

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

    public ProjectDto getProject(Long projectSeq){
        Project project = projectRepository.findById(projectSeq)
                .orElseThrow(() -> new ProjectNotFoundException());
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
                        .map(projectAuthorities -> new ProjectMemberSeqDto(projectAuthorities.getMemberSeq()))
                        .collect(Collectors.toList())))
                .build();
    }

    @Transactional
    public ProjectSeqDto createProject(ProjectCreateRequestDto projectCreateRequest){
        Project newProject = Project.builder()
                .memberSeq(projectCreateRequest.getMemberSeq())
                .projectName(projectCreateRequest.getProjectName())
                .projectDescription(projectCreateRequest.getProjectDescription())
                .projectStatus(projectCreateRequest.getProjectStatus())
                .projectCreatedAt(LocalDateTime.now())
                .build();
        return new ProjectSeqDto(projectRepository.save(newProject).getProjectSeq());
    }

    /**
     * valid check 작성자와 수정하는 사람이 같아야 함
     * @param projectSeq
     * @param projectRequest
     * @return projectSeq
     */
    //TODO 안됨
    @Transactional
    public ProjectSeqDto updateProject(Long projectSeq, ProjectUpdateRequestDto projectRequest) {
        Project project = projectRepository.findById(projectSeq)
                .orElseThrow(()->new ProjectNotFoundException());
        if(Objects.nonNull(projectRequest.getProjectName())){
            project.setProjectName(projectRequest.getProjectName());
        }
        if(Objects.nonNull(projectRequest.getProjectDescription())){
            project.setProjectDescription(projectRequest.getProjectDescription());
        }
        if(Objects.nonNull(projectRequest.getProjectStatus())){
            project.setProjectStatus(projectRequest.getProjectStatus());
        }

        return new ProjectSeqDto(projectRepository.save(project).getProjectSeq());
    }


}
