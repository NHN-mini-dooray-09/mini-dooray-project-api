package com.nhnacademy.minidoorayprojectapi.domain.project.dao;

import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.ProjectAuthority;
import com.nhnacademy.minidoorayprojectapi.global.exception.ProjectNotFoundException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class ProjectAuthoritiesRepositoryTest {
    @Autowired
    ProjectAuthorityRepository projectAuthoritiesRepository;
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

        ProjectAuthority newProjectMember = ProjectAuthority.builder()
                .projectAuthoritiesPk(new ProjectAuthority.ProjectAuthoritiesPk(1L, 1L))
                .project(projectRepository.findById(project1.getProjectSeq()).orElseThrow(()->new ProjectNotFoundException("프로젝트")))
                .authority("PROJECT_ROLE_TEST")
                .build();
            ProjectAuthority actual = projectAuthoritiesRepository.save(newProjectMember);
        assertThat(actual.getProjectAuthoritiesPk().getMemberSeq()).isEqualTo(project1.getMemberSeq());
        assertThat(actual.getProject().getProjectSeq()).isEqualTo(project1.getProjectSeq());
    }

    @Test
    @Order(2)
    void testFindAllByProjectAuthoritiesMembers(){
        Project project1 = Project.builder()
                .memberSeq(1L)
                .projectName("project1")
                .projectDescription("test project1")
                .projectStatus("할일")
                .build();
        projectRepository.save(project1);
        ProjectAuthority projectMember1 = ProjectAuthority.builder()
                .projectAuthoritiesPk(new ProjectAuthority.ProjectAuthoritiesPk(project1.getProjectSeq(), 2L))
                .project(projectRepository.findById(project1.getProjectSeq()).orElseThrow(()->new ProjectNotFoundException("프로젝트")))
                .authority("PROJECT_ROLE_MEMBER")
                .build();
        ProjectAuthority projectMember2 = ProjectAuthority.builder()
                .projectAuthoritiesPk(new ProjectAuthority.ProjectAuthoritiesPk(project1.getProjectSeq(), 3L))
                .project(projectRepository.findById(project1.getProjectSeq()).orElseThrow(()->new ProjectNotFoundException("프로젝트")))
                .authority("PROJECT_ROLE_ADMIN")
                .build();
        ProjectAuthority projectMember3 = ProjectAuthority.builder()
                .projectAuthoritiesPk(new ProjectAuthority.ProjectAuthoritiesPk(project1.getProjectSeq(), 4L))
                .project(projectRepository.findById(project1.getProjectSeq()).orElseThrow(()->new ProjectNotFoundException("프로젝트")))
                .authority("PROJECT_ROLE_MEMBER")
                .build();
        projectAuthoritiesRepository.save(projectMember1);
        projectAuthoritiesRepository.save(projectMember2);
        projectAuthoritiesRepository.save(projectMember3);

        List<ProjectAuthority> actual = projectAuthoritiesRepository.findAllByProjectAuthoritiesPk_ProjectSeqAndAuthority
                (project1.getProjectSeq(), "PROJECT_ROLE_MEMBER");

        assertThat(actual).hasSize(2);

    }
}