package com.nhnacademy.minidoorayprojectapi.domain.milestone.dao;

import com.nhnacademy.minidoorayprojectapi.domain.milestone.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MilestoneRepository extends JpaRepository<Milestone,Long> {
}
