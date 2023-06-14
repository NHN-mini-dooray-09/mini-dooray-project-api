package com.nhnacademy.minidoorayprojectapi.domain.project.dao;

import com.nhnacademy.minidoorayprojectapi.domain.project.entity.ProjectAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectAuthorityRepository extends JpaRepository<ProjectAuthority, ProjectAuthority.ProjectAuthoritiesPk> {
    List<ProjectAuthority> findAllByProjectAuthoritiesPk_ProjectSeqAndAuthority(Long projectSeq, String projectAuthority);
}
