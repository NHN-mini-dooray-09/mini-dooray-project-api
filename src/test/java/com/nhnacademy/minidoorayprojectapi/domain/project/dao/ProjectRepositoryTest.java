package com.nhnacademy.minidoorayprojectapi.domain.project.dao;

import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

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







}