package com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class MilestoneCreateRequestDto {
    @NotBlank
    private String milestoneName;
    private LocalDateTime milestoneStartDate;
    @NotNull
    private LocalDateTime milestoneEndDate;
}
