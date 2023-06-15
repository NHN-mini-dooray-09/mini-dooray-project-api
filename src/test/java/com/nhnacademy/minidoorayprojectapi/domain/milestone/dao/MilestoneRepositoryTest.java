package com.nhnacademy.minidoorayprojectapi.domain.milestone.dao;

import com.nhnacademy.minidoorayprojectapi.domain.milestone.entity.Milestone;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.global.exception.ProjectNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class MilestoneRepositoryTest {
    @Autowired
    TestEntityManager testEntityManager;
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
        testEntityManager.persist(project1);

    }

    @Test
    @Order(1)
    void testFindMilestoneByProject_ProjectSeq() {
        Milestone milestone1 = Milestone.builder()
                .project(project1)
                .build();
        milestoneRepository.save(milestone1);

        List<Milestone> actual = milestoneRepository.findByProject_ProjectSeq(project1.getProjectSeq());
        assertThat(actual).hasSize(1);
    }

    @Test
    @Order(2)
    void testUpdateMilestone() {
        Milestone milestone1 = Milestone.builder()
                .project(project1)
                .build();
        milestoneRepository.save(milestone1);

        Milestone milestone2 = milestoneRepository.findById(milestone1.getMilestoneSeq())
                .orElseThrow(() -> new ProjectNotFoundException("마일스톤 없음"));
        milestone2.updateMilestone("milestone2-change", LocalDateTime.now(),null);

        Milestone actual = milestoneRepository.findById(milestone1.getMilestoneSeq())
                .orElseThrow(() -> new ProjectNotFoundException("마일스톤 없음"));
        assertThat(actual.getMilestoneName()).isEqualTo("milestone2-change");
    }

    @Test
    @Order(3)
    void testDeleteMilestone() {
        Milestone milestone1 = Milestone.builder()
                .project(project1)
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
                .project(project1)
                .build();
        milestoneRepository.save(milestone4);
        Milestone actual = milestoneRepository.findByProject_ProjectSeqAndMilestoneSeq(project1.getProjectSeq(), milestone4.getMilestoneSeq())
                .orElseThrow(()->new ProjectNotFoundException("마일스톤 존재안함"));
        assertThat(actual.getMilestoneSeq()).isEqualTo(milestone4.getMilestoneSeq());
    }
}