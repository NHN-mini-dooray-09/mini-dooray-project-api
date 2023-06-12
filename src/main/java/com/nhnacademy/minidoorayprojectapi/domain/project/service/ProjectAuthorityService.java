package com.nhnacademy.minidoorayprojectapi.domain.project.service;

import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectAuthorityRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectAuthorityDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.ProjectAuthority;
import com.nhnacademy.minidoorayprojectapi.global.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProjectAuthorityService {
    private final ProjectAuthorityRepository projectAuthorityRepository;

    public ProjectAuthorityDto getProjectAuthority(Long projectSeq, Long memberSeq){
        ProjectAuthority projectAuthority = projectAuthorityRepository.findById
                (new ProjectAuthority.ProjectAuthoritiesPk(projectSeq,memberSeq))
                .orElseThrow(()->new UnauthorizedAccessException("프로젝트 접근 권한이 없습니다."));
        return convertToProjectAuthorityDto(projectAuthority);
    }

    private ProjectAuthorityDto convertToProjectAuthorityDto(ProjectAuthority projectAuthority){
        return new ProjectAuthorityDto(projectAuthority.getProject().getMemberSeq(), projectAuthority.getProjectAuthority());
    }




}
