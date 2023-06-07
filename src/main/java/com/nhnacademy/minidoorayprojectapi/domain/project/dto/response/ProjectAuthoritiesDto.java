package com.nhnacademy.minidoorayprojectapi.domain.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectAuthoritiesDto {
    private Long projectSeq;
    private Long memberSeq;
    private String projectAuthority;

}
