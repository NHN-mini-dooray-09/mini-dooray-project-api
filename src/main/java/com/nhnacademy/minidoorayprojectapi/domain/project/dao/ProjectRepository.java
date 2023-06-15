package com.nhnacademy.minidoorayprojectapi.domain.project.dao;

import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {
    boolean existsByProjectSeq(Long projectSeq);
    Page<Project> getAllBy(Pageable pageable);
    Page<Project> getAllByMemberSeq(Pageable pageable, Long memberSeq);

}
