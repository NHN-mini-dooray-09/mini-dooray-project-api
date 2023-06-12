package com.nhnacademy.minidoorayprojectapi.domain.project.dao;

import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.global.exception.ProjectNotFoundException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Test
    @Order(1)
    void testSave(){
        Project project1 = Project.builder()
                .memberSeq(1L)
                .projectName("project1")
                .projectDescription("test project1")
                .projectStatus("할일")
                .build();
        projectRepository.save(project1);

        assertThat(projectRepository.getReferenceById(1L).getProjectSeq()).isEqualTo(1L);
    }

    @Test
    @Order(2)
    void testGetProjectAllPage(){
        Project project1 = Project.builder()
                .memberSeq(1L)
                .projectName("project1")
                .projectDescription("test project1")
                .projectStatus("할일")
                .build();
        Project project2 = Project.builder()
                .memberSeq(2L)
                .projectName("project2")
                .projectDescription("test project2")
                .projectStatus("지연")
                .build();
        projectRepository.save(project1);
        projectRepository.save(project2);
        Pageable pageable = Pageable.ofSize(5);
        Page<Project> actual = projectRepository.getAllBy(pageable);
        assertThat(actual.getContent()).hasSize(2);
    }

    @Test
    @Order(2)
    void testGetAllByMemberSeqPage(){
        Project project3 = Project.builder()
                .memberSeq(1L)
                .projectName("project1")
                .projectDescription("test project1")
                .projectStatus("할일")
                .build();
        Project project4 = Project.builder()
                .memberSeq(1L)
                .projectName("project2")
                .projectDescription("test project2")
                .projectStatus("지연")
                .build();
        projectRepository.save(project3);
        projectRepository.save(project4);
        Pageable pageable = Pageable.ofSize(5);
        Page<Project> actual = projectRepository.getAllByMemberSeq(pageable,1L);

        assertThat(actual.getContent()).hasSize(2);
    }

    @Test
    @Order(3)
    void testFindById(){
        Project project5 = Project.builder()
                .memberSeq(2L)
                .projectName("project2")
                .projectDescription("test project2")
                .projectStatus("지연")
                .build();
        projectRepository.save(project5);
        Project actual = projectRepository.findById(project5.getProjectSeq())
                .orElseThrow(() -> new ProjectNotFoundException("프로젝트를 찾을 수 없습니다."));

        assertThat(actual.getProjectSeq()).isEqualTo(project5.getProjectSeq());
    }







}