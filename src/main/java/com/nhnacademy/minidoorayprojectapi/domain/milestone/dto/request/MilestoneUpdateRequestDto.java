package com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class MilestoneUpdateRequestDto {
    @NotBlank
    private String milestoneName;
    @NotNull
    private LocalDateTime milestoneStartDate;
    @NotNull
    private LocalDateTime milestoneEndDate;
}
