package com.nhnacademy.minidoorayprojectapi.domain.task.dao;

import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.Task;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.TaskTag;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskRepositoryTest {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskTagRepository taskTagRepository;

    Project project1;

    @BeforeEach
    public void setUp(){
        project1 = Project.builder()
                .build();
        projectRepository.save(project1);
    }

    @Test
    @Order(1)
    void testUpdateTask(){
        Task task1 = Task.builder()
                .build();
        taskRepository.save(task1);

        Task task2 = taskRepository.findById(task1.getTaskSeq())
                .orElse(null);
        task2.updateTask("task1-update",null,null,null,null);

        assertThat(task1.getTaskTitle()).isEqualTo(task2.getTaskTitle());

    }

    @Test
    void testDeleteTask(){
        Task task2 = Task.builder()
                .build();
        taskRepository.save(task2);

        taskRepository.deleteById(task2.getTaskSeq());

        Task expected = taskRepository.findById(task2.getTaskSeq())
                .orElse(null);

        assertNull(expected);
    }

    @Test
    void testGetTaskAllByProject_ProjectSeq() {
        Task task3 = Task.builder()
                .project(project1)
                .build();
        Task task4 = Task.builder()
                .project(project1)
                .build();
        Task task5 = Task.builder()
                .project(project1)
                .build();
        taskRepository.save(task3);
        taskRepository.save(task4);
        taskRepository.save(task5);

        Pageable pageable = PageRequest.of(0,2);
        Page<Task> expected = taskRepository.getAllByProject_ProjectSeq(pageable,project1.getProjectSeq());
        assertEquals(2,expected.getContent().size());

    }

    @Test
    void testFindTaskByProject_ProjectSeqAndTaskSeq() {
        Task task1 = Task.builder()
                .project(project1)
                .build();
        taskRepository.save(task1);
        Task expected = taskRepository.findByProject_ProjectSeqAndTaskSeq(project1.getProjectSeq(), task1.getTaskSeq())
                .orElse(null);
        assertThat(expected).isNotNull();

    }

    @Test
    void testExistsTaskByTaskSeqAndAndMemberSeq() {
        Task task1 = Task.builder()
                .memberSeq(1L)
                .build();
        taskRepository.save(task1);

        boolean expected = taskRepository.existsByTaskSeqAndAndMemberSeq(task1.getTaskSeq(),1L);

        assertTrue(expected);

    }
}