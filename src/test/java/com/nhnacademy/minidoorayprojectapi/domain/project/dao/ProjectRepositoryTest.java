package com.nhnacademy.minidoorayprojectapi.domain.project.dao;

import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Test
    void testSave(){
        Project project1 = Project.builder()
                .memberSeq(1L)
                .projectName("project1")
                .projectDescription("test project1")
                .projectStatus("할일")
                .projectCreatedAt(LocalDateTime.now())
                .build();
        projectRepository.save(project1);

        assertThat(projectRepository.getReferenceById(1L).getProjectSeq()).isEqualTo(1L);
    }

    @Test
    void testSave2(){
        Project project2 = Project.builder()
                .memberSeq(1L)
                .projectName("project1")
                .projectDescription("test project1")
                .projectStatus("할일")
                .projectCreatedAt(LocalDateTime.now())
                .build();
        Long result = projectRepository.save(project2).getProjectSeq();

        assertThat(projectRepository.getReferenceById(1L).getProjectSeq()).isEqualTo(1L);
    }



}