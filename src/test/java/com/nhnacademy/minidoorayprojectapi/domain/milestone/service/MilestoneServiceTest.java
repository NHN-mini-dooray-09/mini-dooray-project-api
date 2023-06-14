package com.nhnacademy.minidoorayprojectapi.domain.milestone.service;

import com.nhnacademy.minidoorayprojectapi.domain.milestone.dao.MilestoneRepository;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.request.MilestoneCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.request.MilestoneUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.response.MilestoneDto;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.response.MilestoneSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.entity.Milestone;
import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.global.exception.ProjectNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MilestoneServiceTest {

    @InjectMocks
    MilestoneService milestoneService;
    @Mock
    MilestoneRepository milestoneRepository;
    @Mock
    ProjectRepository projectRepository;


    @Test
    void testGetMilestonesByProjectSeq() {
        List<Milestone> milestones = new ArrayList<>(
                Arrays.asList(Milestone.builder().build())
        );
        Project project1 = Project.builder()
                .build();
        project1.updateProject(null,null,null,
                null,null,milestones,null);

        when(projectRepository.findById(any()))
                .thenReturn(Optional.ofNullable(project1));

        List<MilestoneDto> result = milestoneService.getMilestonesByProjectSeq(any());

        verify(projectRepository, times(1)).findById(any());
        assertEquals(1,result.size());
    }

    @Test
    void testCreateMilestone() {

        when(projectRepository.findById(any()))
                .thenReturn(Optional.ofNullable(mock(Project.class)));

        MilestoneCreateRequestDto milestoneCreateRequestDto = MilestoneCreateRequestDto
                .builder()
                .build();

        MilestoneSeqDto result = milestoneService.createMilestone(any(),milestoneCreateRequestDto);

        assertNotNull(result);
        verify(milestoneRepository, times(1))
                .save(any());
    }

    @Test
    void testUpdateMilestone() {
        Milestone milestone = Milestone.builder().build();

        when(milestoneRepository.findByProject_ProjectSeqAndMilestoneSeq(anyLong(),anyLong()))
                .thenReturn(Optional.ofNullable(milestone));

        MilestoneUpdateRequestDto milestoneUpdateRequest = MilestoneUpdateRequestDto.builder()
                .milestoneName("milestone-update")
                .build();

        milestoneService.updateMilestone(anyLong(), anyLong(), milestoneUpdateRequest);

        assertThat(milestone.getMilestoneName()).isEqualTo(milestoneUpdateRequest.getMilestoneName());
        verify(milestoneRepository, times(1))
                .findByProject_ProjectSeqAndMilestoneSeq(anyLong(),anyLong());

    }

    @Test
    @DisplayName("마일스톤 삭제")
    void testDeleteMilestone() {
        milestoneService.deleteMilestone(any());

        verify(milestoneRepository, times(1))
                .deleteById(any());
    }

    @Test
    @DisplayName("createMilestone NotFoundException")
    void testCreateMilestoneInProjectNotFoundException(){
        when(projectRepository.findById(any()))
                .thenReturn(Optional.empty());

        MilestoneCreateRequestDto milestoneCreateRequestDto = MilestoneCreateRequestDto
                .builder()
                .build();

        assertThrows(ProjectNotFoundException.class,
                () -> milestoneService.createMilestone(any(),milestoneCreateRequestDto));

    }
}