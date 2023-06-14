package com.nhnacademy.minidoorayprojectapi.domain.task.service;

import com.nhnacademy.minidoorayprojectapi.domain.milestone.dao.MilestoneRepository;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.entity.Milestone;
import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dao.TagRepository;
import com.nhnacademy.minidoorayprojectapi.domain.tag.entity.Tag;
import com.nhnacademy.minidoorayprojectapi.domain.task.dao.TaskRepository;
import com.nhnacademy.minidoorayprojectapi.domain.task.dao.TaskTagRepository;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.request.TaskCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.request.TaskUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskSeqNameAndMemberSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.Task;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.TaskTag;
import com.nhnacademy.minidoorayprojectapi.global.exception.AccessDeniedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @InjectMocks
    TaskService taskService;
    @Mock
    TaskRepository taskRepository;
    @Mock
    TaskTagRepository taskTagRepository;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    TagRepository tagRepository;
    @Mock
    MilestoneRepository milestoneRepository;

    @Test
    void testGetTasks() {
        List<Task> tasks = new ArrayList<>(
                Arrays.asList(mock(Task.class),mock(Task.class))
        );

        Pageable pageable = PageRequest.of(0,3);

        Page<Task> taskPage = new PageImpl<>(tasks, pageable,tasks.size());

        when(taskRepository.getAllByProject_ProjectSeq(any(Pageable.class), anyLong()))
                .thenReturn(taskPage);

        Page<TaskSeqNameAndMemberSeqDto> result = taskService.getTasks(pageable,1L);

        assertEquals(2, result.getContent().size());
        verify(taskRepository, times(1))
                .getAllByProject_ProjectSeq(any(Pageable.class),anyLong());


    }

    @Test
    void testGetTask() {
        Task task = Task.builder()
                .milestone(new Milestone())
                .build();

        List<TaskTag> taskTags = new ArrayList<>(
                Arrays.asList(TaskTag.builder()
                                .tag(new Tag())
                                .build(),
                        TaskTag.builder()
                                .tag(new Tag())
                                .build()
                        )
        );

        when(taskRepository.findByProject_ProjectSeqAndTaskSeq(anyLong(),anyLong()))
                .thenReturn(Optional.ofNullable(task));

        when(taskTagRepository.findAllByTask_TaskSeq(any()))
                .thenReturn(taskTags);

        taskService.getTask(1L,1L);

        verify(taskRepository, times(1))
                .findByProject_ProjectSeqAndTaskSeq(anyLong(),anyLong());

        verify(taskTagRepository, times(1))
                .findAllByTask_TaskSeq(anyLong());

    }

    @Test
    void testCreateTask() {
        TaskCreateRequestDto taskCreateRequest = TaskCreateRequestDto.builder()
                .tags(Arrays.asList(1L))
                .build();

        when(projectRepository.findById(anyLong()))
                .thenReturn(Optional.of(mock(Project.class)));
        when(taskRepository.save(any()))
                .thenReturn(mock(Task.class));
        when(tagRepository.findByProject_ProjectSeqAndTagSeq(anyLong(),anyLong()))
                .thenReturn(Optional.of(mock(Tag.class)));
        when(taskTagRepository.save(any()))
                .thenReturn(mock(TaskTag.class));

        taskService.createTask(1L,1L,taskCreateRequest);

        verify(projectRepository, times(1))
                .findById(anyLong());
        verify(taskRepository, times(1))
                .save(any());
        verify(tagRepository, times(1))
                .findByProject_ProjectSeqAndTagSeq(anyLong(),anyLong());
        verify(taskTagRepository, times(1))
                .save(any());

    }

    @Test
    void testUpdateTask() {
        Task task = Task.builder()
                .memberSeq(1L)
                .build();
        TaskUpdateRequestDto taskUpdateRequest = TaskUpdateRequestDto.builder()
                .taskContent("task-update")
                .tags(Arrays.asList(1L, 2L))
                .build();

        when(taskRepository.findByProject_ProjectSeqAndTaskSeq(any(),any()))
                .thenReturn(Optional.ofNullable(task));
        when(milestoneRepository.findByProject_ProjectSeqAndMilestoneSeq(any(),any()))
                .thenReturn(Optional.ofNullable(new Milestone()));
        when(tagRepository.findByProject_ProjectSeqAndTagSeq(any(),any()))
                .thenReturn(Optional.ofNullable(new Tag()));

        taskService.updateTask(1L,1L,1L, taskUpdateRequest);

        assertThat(task.getTaskContent()).isEqualTo(taskUpdateRequest.getTaskContent());

        verify(taskRepository, times(1))
                .findByProject_ProjectSeqAndTaskSeq(any(),any());
        verify(milestoneRepository, times(1))
                .findByProject_ProjectSeqAndMilestoneSeq(any(),any());
        verify(tagRepository, times(taskUpdateRequest.getTags().size()))
                .findByProject_ProjectSeqAndTagSeq(any(),any());
    }
    @Test
    void testUpdateTaskAccessDeniedException() {
        Task task = Task.builder()
                .memberSeq(1L)
                .build();
        TaskUpdateRequestDto taskUpdateRequest = TaskUpdateRequestDto.builder()
                .taskContent("task-update")
                .tags(Arrays.asList(1L, 2L))
                .build();

        when(taskRepository.findByProject_ProjectSeqAndTaskSeq(any(),any()))
                .thenReturn(Optional.ofNullable(task));

        assertThrows(AccessDeniedException.class,
                () -> taskService.updateTask(1L,1L,12L, taskUpdateRequest));

        verify(taskRepository, times(1))
                .findByProject_ProjectSeqAndTaskSeq(any(),any());
    }


    @Test
    void testDeleteTask() {
        when(taskRepository.existsByTaskSeqAndAndMemberSeq(anyLong(),anyLong()))
                .thenReturn(true);

        taskService.deleteTask(1L,1L);

        verify(taskRepository, times(1))
                .deleteById(anyLong());
    }
    @Test
    void testDeleteTaskAccessDeniedException() {
        when(taskRepository.existsByTaskSeqAndAndMemberSeq(anyLong(),anyLong()))
                .thenReturn(false);

        assertThrows(AccessDeniedException.class,
                () -> taskService.deleteTask(1L,1L));

        verify(taskRepository, times(1))
                .existsByTaskSeqAndAndMemberSeq(anyLong(),anyLong());
    }



}