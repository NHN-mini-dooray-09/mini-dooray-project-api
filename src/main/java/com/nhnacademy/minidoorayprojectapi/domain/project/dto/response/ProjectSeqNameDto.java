package com.nhnacademy.minidoorayprojectapi.domain.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProjectSeqNameDto {
    private Long projectSeq;
    private String projectName;
    private Long memberSeq;

}
