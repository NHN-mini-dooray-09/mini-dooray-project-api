package com.nhnacademy.minidoorayprojectapi.domain.tag.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagSeqDto {
    private Long tagSeq;

    @Builder
    public TagSeqDto(Long tagSeq) {
        this.tagSeq = tagSeq;
    }
}
