package com.nhnacademy.minidoorayprojectapi.domain.tag.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagSeqNameDto {
    private Long tagSeq;
    private String tagName;

    @Builder
    public TagSeqNameDto(Long tagSeq, String tagName) {
        this.tagSeq = tagSeq;
        this.tagName = tagName;
    }
}
