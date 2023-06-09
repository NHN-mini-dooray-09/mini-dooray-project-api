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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class MilestoneService {
    private final MilestoneRepository milestoneRepository;
    private final ProjectRepository projectRepository;

    /**
     * 마일스톤 전체 조회
     * @param projectSeq
     * @return
     */

    public List<MilestoneDto> getMilestonesByProjectSeq(Long projectSeq){
        if(!projectRepository.existsByProjectSeq(projectSeq)){
            throw new ProjectNotFoundException("프토젝트를 찾을 수 없습니다.");
        }
        List<Milestone> milestones = milestoneRepository.findByProject_ProjectSeq(projectSeq);
        return convertToMilestoneDtoList(milestones);
    }

    private List<MilestoneDto> convertToMilestoneDtoList(List<Milestone> milestones){
        return milestones.stream()
                .map(milestone -> new MilestoneDto(milestone.getMilestoneSeq(),
                        milestone.getMilestoneName(),
                        milestone.getMilestoneStartDate(),
                        milestone.getMilestoneEndDate()))
                .collect(Collectors.toList());
    }

    @Transactional
    public MilestoneSeqDto createMilestone(Long projectSeq, MilestoneCreateRequestDto milestoneCreateRequest){

        Milestone newMilestone = Milestone.builder()
                .milestoneName(milestoneCreateRequest.getMilestoneName())
                .milestoneStartDate(milestoneCreateRequest.getMilestoneStartDate())
                .milestoneEndDate(milestoneCreateRequest.getMilestoneEndDate())
                .project(projectRepository.findById(projectSeq).orElseThrow(
                        () -> new ProjectNotFoundException("프로젝트를 찾을 수 없음")))
                .build();
        milestoneRepository.save(newMilestone);
        return convertToMilestoneSeqDto(newMilestone);
    }

    @Transactional
    public MilestoneSeqDto updateMilestone(Long projectSeq,Long milestoneSeq, MilestoneUpdateRequestDto milestoneUpdateRequest){

        Milestone milestone = milestoneRepository.findByProject_ProjectSeqAndMilestoneSeq(projectSeq,milestoneSeq)
                        .orElseThrow(() -> new ProjectNotFoundException("마일스톤을 찾을 수 없습니다."));
        milestone.updateMilestone(milestoneUpdateRequest.getMilestoneName(),
                milestoneUpdateRequest.getMilestoneStartDate(),
                milestoneUpdateRequest.getMilestoneEndDate());
        return convertToMilestoneSeqDto(milestone);
    }

    private MilestoneSeqDto convertToMilestoneSeqDto(Milestone milestone){
        return new MilestoneSeqDto(milestone.getMilestoneSeq());
    }

    @Transactional
    public void deleteMilestone(Long milestoneSeq) {
        milestoneRepository.deleteById(milestoneSeq);
    }


}
