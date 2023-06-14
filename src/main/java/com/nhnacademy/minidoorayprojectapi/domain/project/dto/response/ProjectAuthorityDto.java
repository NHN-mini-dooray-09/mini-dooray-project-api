package com.nhnacademy.minidoorayprojectapi.domain.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectAuthorityDto {
    private Long memberSeq;
    private String projectAuthority;

//    private Long projectSeq;
}
