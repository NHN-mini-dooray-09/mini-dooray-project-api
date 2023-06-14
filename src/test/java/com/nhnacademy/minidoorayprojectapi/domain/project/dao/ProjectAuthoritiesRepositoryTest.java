package com.nhnacademy.minidoorayprojectapi.domain.project.dao;

import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.ProjectAuthority;
import com.nhnacademy.minidoorayprojectapi.global.exception.ProjectNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class ProjectAuthoritiesRepositoryTest {
    @Autowired
    TestEntityManager testEntityManager;
    @Autowired
    ProjectAuthorityRepository projectAuthoritiesRepository;

    Project project1;

    @BeforeEach
    public void setUp(){
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
    void testSave(){


        ProjectAuthority newProjectMember = ProjectAuthority.builder()
                .projectAuthoritiesPk(new ProjectAuthority.ProjectAuthoritiesPk(1L, 1L))
                .project(project1)
                .authority("PROJECT_ROLE_TEST")
                .build();
            ProjectAuthority actual = projectAuthoritiesRepository.save(newProjectMember);
        assertThat(actual.getProjectAuthoritiesPk().getMemberSeq()).isEqualTo(project1.getMemberSeq());
        assertThat(actual.getProject().getProjectSeq()).isEqualTo(project1.getProjectSeq());
    }

    @Test
    @Order(2)
    void testFindAllByProjectAuthoritiesMembers(){

        ProjectAuthority projectMember1 = ProjectAuthority.builder()
                .projectAuthoritiesPk(new ProjectAuthority.ProjectAuthoritiesPk(project1.getProjectSeq(), 2L))
                .project(project1)
                .authority("PROJECT_ROLE_MEMBER")
                .build();
        ProjectAuthority projectMember2 = ProjectAuthority.builder()
                .projectAuthoritiesPk(new ProjectAuthority.ProjectAuthoritiesPk(project1.getProjectSeq(), 3L))
                .project(project1)
                .authority("PROJECT_ROLE_ADMIN")
                .build();
        ProjectAuthority projectMember3 = ProjectAuthority.builder()
                .projectAuthoritiesPk(new ProjectAuthority.ProjectAuthoritiesPk(project1.getProjectSeq(), 4L))
                .project(project1)
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