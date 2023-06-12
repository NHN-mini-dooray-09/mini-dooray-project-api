package com.nhnacademy.minidoorayprojectapi.domain.project.dao;

import com.nhnacademy.minidoorayprojectapi.domain.project.entity.ProjectAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectAuthorityRepository extends JpaRepository<ProjectAuthority, ProjectAuthority.ProjectAuthoritiesPk> {
}
