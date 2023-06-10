package com.nhnacademy.minidoorayprojectapi.domain.project.dao;

import com.nhnacademy.minidoorayprojectapi.domain.project.entity.ProjectAuthorities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectAuthoritiesRepository extends JpaRepository<ProjectAuthorities, ProjectAuthorities.ProjectAuthoritiesPk> {
}
