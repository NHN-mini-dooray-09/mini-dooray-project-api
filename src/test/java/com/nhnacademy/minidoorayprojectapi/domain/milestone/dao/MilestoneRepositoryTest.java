package com.nhnacademy.minidoorayprojectapi.domain.milestone.dao;

import com.nhnacademy.minidoorayprojectapi.domain.milestone.entity.Milestone;
import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.global.exception.ProjectNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class MilestoneRepositoryTest {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    MilestoneRepository milestoneRepository;

    Project project1;

    @BeforeEach
    public void setUp() {
        project1 = Project.builder()
                .memberSeq(1L)
                .projectName("project1")
                .projectDescription("test project1")
                .projectStatus("할일")
                .build();
        projectRepository.save(project1);

    }

    @Test
    @Order(1)
    void testFindMilestoneByProject_ProjectSeq() {
        Milestone milestone1 = Milestone.builder()
                .project(projectRepository.findById(project1.getProjectSeq()).orElseThrow(()->new ProjectNotFoundException("프로젝트 없음")))
                .build();
        milestoneRepository.save(milestone1);
        List<Milestone> actual = milestoneRepository.findByProject_ProjectSeq(1L);
        assertThat(actual).hasSize(1);
    }

    @Test
    @Order(2)
    void testUpdateMilestone() {
        Milestone milestone2 = Milestone.builder()
                .project(projectRepository.findById(project1.getProjectSeq()).orElseThrow(()->new ProjectNotFoundException("프로젝트 없음")))
                .build();
        milestoneRepository.save(milestone2);
        Milestone milestone3 = milestoneRepository.findById(milestone2.getMilestoneSeq())
                .orElseThrow(() -> new ProjectNotFoundException("마일스톤 없음"));
        milestone3.updateMilestone("milestone2-change", LocalDateTime.now(),null);

        Milestone actual = milestoneRepository.findById(milestone2.getMilestoneSeq())
                .orElseThrow(() -> new ProjectNotFoundException("마일스톤 없음"));
        assertThat(actual.getMilestoneName()).isEqualTo("milestone2-change");
    }

    @Test
    @Order(3)
    void testDeleteMilestone() {
        Milestone milestone1 = Milestone.builder()
                .project(projectRepository.findById(project1.getProjectSeq()).orElseThrow(()->new ProjectNotFoundException("프로젝트 없음")))
                .build();
        milestoneRepository.save(milestone1);
        milestoneRepository.deleteById(milestone1.getMilestoneSeq());
        Milestone actual = milestoneRepository.findById(milestone1.getMilestoneSeq())
                .orElse(null);
        assertNull(actual);
    }

    @Test
    @Order(4)
    void testFindByProject_ProjectSeqAndMilestoneSeq() {
        Milestone milestone4 = Milestone.builder()
                .project(projectRepository.findById(project1.getProjectSeq()).orElseThrow(()->new ProjectNotFoundException("프로젝트 없음")))
                .build();
        milestoneRepository.save(milestone4);
        Milestone actual = milestoneRepository.findByProject_ProjectSeqAndMilestoneSeq(project1.getProjectSeq(), milestone4.getMilestoneSeq())
                .orElseThrow(()->new ProjectNotFoundException("마일스톤 존재안함"));
        assertThat(actual.getMilestoneSeq()).isEqualTo(milestone4.getMilestoneSeq());
    }
}