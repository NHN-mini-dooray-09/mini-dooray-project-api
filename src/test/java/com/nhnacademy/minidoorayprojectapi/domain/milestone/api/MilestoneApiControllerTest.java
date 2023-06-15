package com.nhnacademy.minidoorayprojectapi.domain.milestone.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.request.MilestoneCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.request.MilestoneUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.response.MilestoneDto;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.response.MilestoneSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.service.MilestoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MilestoneApiController.class)
class MilestoneApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    MilestoneService milestoneService;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(){
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getMilestones() throws Exception{
        List<MilestoneDto> milestoneDtoList = Arrays.asList(
                new MilestoneDto(),new MilestoneDto()
        );

        when(milestoneService.getMilestonesByProjectSeq(any()))
                .thenReturn(milestoneDtoList);

        mockMvc.perform(get("/projects/1/milestones"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$",hasSize(2)));

        verify(milestoneService, times(1))
                .getMilestonesByProjectSeq(any());
    }

    @Test
    void createMilestone() throws Exception{
        MilestoneCreateRequestDto milestoneCreateRequestDto = MilestoneCreateRequestDto.builder()
                .milestoneName("milestone1")
                .milestoneEndDate(LocalDateTime.now())
                .build();
        MilestoneSeqDto milestoneSeqDto = new MilestoneSeqDto(1L);

        when(milestoneService.createMilestone(any(),any()))
                .thenReturn(milestoneSeqDto);

        mockMvc.perform(post("/projects/1/milestones")
                        .content(objectMapper.writeValueAsString(milestoneCreateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.milestoneSeq",equalTo(milestoneSeqDto.getMilestoneSeq().intValue())));

        verify(milestoneService, times(1))
                .createMilestone(any(),any());
    }

    @Test
    void createMilestone_ValidationFailed() throws Exception{
        MilestoneCreateRequestDto milestoneCreateRequestDto = MilestoneCreateRequestDto.builder()
                .milestoneName("milestone1")
                .build();

        mockMvc.perform(post("/projects/1/milestones")
                        .content(objectMapper.writeValueAsString(milestoneCreateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(milestoneService, never())
                .createMilestone(any(),any());
    }

    @Test
    void updateMilestone() throws Exception{
        MilestoneUpdateRequestDto milestoneUpdateRequestDto = MilestoneUpdateRequestDto.builder()
                .milestoneName("milestone-update")
                .milestoneStartDate(LocalDateTime.now())
                .milestoneEndDate(LocalDateTime.now())
                .build();
        MilestoneSeqDto milestoneSeqDto = new MilestoneSeqDto(1L);

        when(milestoneService.updateMilestone(any(),any(),any()))
                .thenReturn(milestoneSeqDto);

        mockMvc.perform(patch("/projects/1/milestones/1")
                        .content(objectMapper.writeValueAsString(milestoneUpdateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(milestoneService, times(1))
                .updateMilestone(any(),any(),any());

    }

    @Test
    void deleteMilestone() throws Exception{

        mockMvc.perform(delete("/projects/1/milestones/1"))
                        .andExpect(status().isNoContent());

        verify(milestoneService, times(1))
                .deleteMilestone(any());
    }
}