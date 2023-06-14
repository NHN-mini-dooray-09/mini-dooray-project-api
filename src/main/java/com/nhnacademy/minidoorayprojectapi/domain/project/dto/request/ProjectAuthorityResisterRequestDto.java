package com.nhnacademy.minidoorayprojectapi.domain.project.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAuthorityResisterRequestDto {
    private List<Long> projectMembers;
}
