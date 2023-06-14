package com.nhnacademy.minidoorayprojectapi.domain.project.service;

import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectAuthorityRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectAuthorityDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.ProjectAuthority;
import com.nhnacademy.minidoorayprojectapi.global.exception.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProjectAuthorityService {
    private final ProjectAuthorityRepository projectAuthorityRepository;

    public ProjectAuthorityDto getProjectAuthority(Long projectSeq, Long memberSeq) {
        ProjectAuthority projectAuthorities = projectAuthorityRepository.findById(new ProjectAuthority.ProjectAuthoritiesPk(projectSeq, memberSeq))
                .orElseThrow(AccessDeniedException::new);

        return convertToProjectAuthorityDto(projectAuthorities);
    }

    private ProjectAuthorityDto convertToProjectAuthorityDto(ProjectAuthority projectAuthority){
        return new ProjectAuthorityDto(projectAuthority.getProject().getMemberSeq(), projectAuthority.getAuthority());
    }


}
