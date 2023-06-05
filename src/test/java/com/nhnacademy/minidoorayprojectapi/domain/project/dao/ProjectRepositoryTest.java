package com.nhnacademy.minidoorayprojectapi.domain.project.dao;

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
        Project project1 = new Project(null,1L,"project1","test project1","할일", LocalDateTime.now());
        projectRepository.save(project1);

        assertThat(projectRepository.getReferenceById(1L).getProjectSeq()).isEqualTo(1L);
    }

    @Test
    void testFindAll(){
        System.out.println(projectRepository.findAll().get(0));
    }

}