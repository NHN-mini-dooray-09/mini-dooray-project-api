package com.nhnacademy.minidoorayprojectapi.domain.task.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.request.TaskCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.request.TaskUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskSeqNameAndMemberSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskApiController.class)
class TaskApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TaskService taskService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetTasksInProject() throws Exception{
        List<TaskSeqNameAndMemberSeqDto> taskSeqNameAndMemberSeqDtoList = Arrays.asList(
                new TaskSeqNameAndMemberSeqDto(1L,"task1",1L)
        );
        Page<TaskSeqNameAndMemberSeqDto> taskSeqNameAndMemberSeqDtoPage = new PageImpl<>(taskSeqNameAndMemberSeqDtoList);

        when(taskService.getTasks(any(),any()))
                .thenReturn(taskSeqNameAndMemberSeqDtoPage);

        mockMvc.perform(get("/projects/1/tasks"))
                .andExpect(jsonPath("$.content",hasSize(1)))
                .andExpect(status().isOk());

        verify(taskService, times(1))
                .getTasks(any(),any());

    }

    @Test
    void testGetTask() throws Exception{
        TaskDto taskDto = TaskDto.builder()
                .build();

        when(taskService.getTask(any(),any()))
                .thenReturn(taskDto);

        mockMvc.perform(get("/projects/1/1"))
                .andExpect(status().isOk());

        verify(taskService, times(1))
                .getTask(any(),any());
    }

    @Test
    void testCreateTask() throws Exception{
        TaskCreateRequestDto taskCreateRequestDto = TaskCreateRequestDto.builder()
                .taskTitle("task1")
                .build();
        TaskSeqDto taskSeqDto = new TaskSeqDto(1L);

        when(taskService.createTask(any(),any(),any()))
                .thenReturn(taskSeqDto);

        mockMvc.perform(post("/projects/1")
                        .param("member-seq","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreateRequestDto)))
                .andExpect(status().isCreated());

        verify(taskService, times(1))
                .createTask(any(),any(),any());
    }

    @Test
    void testCreateTask_ValidationFailed() throws Exception{
        TaskCreateRequestDto taskCreateRequestDto = TaskCreateRequestDto.builder()
                .build();

        mockMvc.perform(post("/projects/1")
                        .param("member-seq","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreateRequestDto)))
                .andExpect(status().isBadRequest());

        verify(taskService, never())
                .createTask(any(),any(),any());
    }

    @Test
    void testUpdateTask() throws Exception{
        TaskUpdateRequestDto taskUpdateRequestDto = TaskUpdateRequestDto.builder()
                .taskContent("test update test")
                .taskStatus("task-status")
                .tags(new ArrayList<>())
                .milestoneSeq(1L)
                .taskTitle("task-update")
                .build();
        TaskSeqDto taskSeqDto = new TaskSeqDto(1L);

        when(taskService.updateTask(any(),any(),any(),any()))
                .thenReturn(taskSeqDto);

        mockMvc.perform(patch("/projects/1/1")
                .param("member-seq","1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskUpdateRequestDto)))
                .andExpect(status().isOk());

        verify(taskService, times(1))
                .updateTask(any(),any(),any(),any());
    }

    @Test
    void testUpdateTask_ValidationFailed() throws Exception{
        TaskUpdateRequestDto taskUpdateRequestDto = TaskUpdateRequestDto.builder()
                .taskContent("test update test")
                .taskStatus("task-status")
                .tags(new ArrayList<>())
                .build();
        TaskSeqDto taskSeqDto = new TaskSeqDto(1L);

        mockMvc.perform(patch("/projects/1/1")
                        .param("member-seq","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskUpdateRequestDto)))
                .andExpect(status().isBadRequest());

        verify(taskService, never())
                .updateTask(any(),any(),any(),any());
    }

    @Test
    void testDeleteTask() throws Exception{
        mockMvc.perform(delete("/projects/1/1")
                        .param("member-seq","1"))
                .andExpect(status().isNoContent());

        verify(taskService, times(1))
                .deleteTask(any(),any());
    }
}