package com.nhnacademy.minidoorayprojectapi.domain.project.dao;

import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.ProjectAuthorities;
import com.nhnacademy.minidoorayprojectapi.domain.project.exception.ProjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("dev")
@Transactional
class ProjectAuthoritiesRepositoryTest {
    @Autowired
    ProjectAuthoritiesRepository projectAuthoritiesRepository;
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

        ProjectAuthorities newProjectMember = ProjectAuthorities.builder()
                .projectAuthoritiesPk(new ProjectAuthorities.ProjectAuthoritiesPk(1L, 1L))
                .project(projectRepository.findById(project1.getProjectSeq()).orElseThrow(ProjectNotFoundException::new))
                .projectAuthority("PROJECT_ROLE_TEST")
                .build();
            Long result = projectAuthoritiesRepository.save(newProjectMember).getProjectAuthoritiesPk().getMemberSeq();
        assertThat(result).isEqualTo(1L);
    }

    @Test
    void testGetProjects(){

        projectRepository.findAll().get(0);
    }


}