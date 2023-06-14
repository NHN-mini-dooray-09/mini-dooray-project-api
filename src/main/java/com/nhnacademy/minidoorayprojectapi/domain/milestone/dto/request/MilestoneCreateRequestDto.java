package com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MilestoneCreateRequestDto {
    @NotBlank
    private String milestoneName;
    private LocalDateTime milestoneStartDate;
    @NotNull
    private LocalDateTime milestoneEndDate;

    @Builder
    public MilestoneCreateRequestDto(String milestoneName, LocalDateTime milestoneStartDate, LocalDateTime milestoneEndDate) {
        this.milestoneName = milestoneName;
        this.milestoneStartDate = milestoneStartDate;
        this.milestoneEndDate = milestoneEndDate;
    }
}
