package com.nhnacademy.minidoorayprojectapi.domain.project.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ProjectAuthoritiesResisterRequestDto {
    private List<Long> projectMembers;
}
