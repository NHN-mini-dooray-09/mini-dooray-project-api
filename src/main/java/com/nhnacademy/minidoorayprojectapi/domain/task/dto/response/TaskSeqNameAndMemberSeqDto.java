package com.nhnacademy.minidoorayprojectapi.domain.task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskSeqNameAndMemberSeqDto {
    private Long taskSeq;
    private String taskTitle;
    private Long memberSeq;

}




