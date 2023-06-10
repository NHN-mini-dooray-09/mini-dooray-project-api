package com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MilestoneDto {
    private Long milestoneSeq;
    private String milestoneName;
    private LocalDateTime milestoneStartDate;
    private LocalDateTime milestoneEndDate;

    @Builder
    public MilestoneDto(Long milestoneSeq, String milestoneName, LocalDateTime milestoneStartDate, LocalDateTime milestoneEndDate) {
        this.milestoneSeq = milestoneSeq;
        this.milestoneName = milestoneName;
        this.milestoneStartDate = milestoneStartDate;
        this.milestoneEndDate = milestoneEndDate;
    }
}
