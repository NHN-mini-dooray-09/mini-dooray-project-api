package com.nhnacademy.minidoorayprojectapi.domain.project.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAuthorityResisterRequestDto {
    @NotNull
    private List<Long> projectMembers;
}
