package com.nhnacademy.minidoorayprojectapi.domain.project.dao;

import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.global.exception.ProjectNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    TestEntityManager testEntityManager;


    @BeforeEach
    public void setUp(){

    }

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
    @Order(1)
    void testUpdateProject(){
        Project project1 = Project.builder()
                .memberSeq(1L)
                .projectName("project1")
                .projectStatus("할일")
                .build();
        projectRepository.save(project1);
        Project excepted = projectRepository.findById(project1.getProjectSeq())
                .orElseThrow(() -> new ProjectNotFoundException("프로젝트 없음"));
        excepted.updateProject("project1-change",null,null,null,null,null,null);

        assertThat(excepted).isEqualTo(project1);
    }




    @Test
    @Order(3)
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
        Page<Project> excepted = projectRepository.getAllBy(pageable);
        assertThat(excepted.getContent()).hasSize(2);
    }

    @Test
    @Order(4)
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
        Page<Project> excepted = projectRepository.getAllByMemberSeq(pageable,1L);

        assertThat(excepted.getContent()).hasSize(2);
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
        Project excepted = projectRepository.findById(project5.getProjectSeq())
                .orElseThrow(() -> new ProjectNotFoundException("프로젝트를 찾을 수 없습니다."));

        assertThat(excepted.getProjectSeq()).isEqualTo(project5.getProjectSeq());
    }







}