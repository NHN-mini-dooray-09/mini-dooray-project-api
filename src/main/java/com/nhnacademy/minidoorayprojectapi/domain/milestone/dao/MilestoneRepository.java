package com.nhnacademy.minidoorayprojectapi.domain.milestone.dao;

import com.nhnacademy.minidoorayprojectapi.domain.milestone.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MilestoneRepository extends JpaRepository<Milestone,Long> {
    List<Milestone> findByProject_ProjectSeq(Long projectSeq);
    Optional<Milestone> findByProject_ProjectSeqAndMilestoneSeq(Long projectSeq, Long milestoneSeq);
}
