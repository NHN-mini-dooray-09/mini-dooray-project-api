package com.nhnacademy.minidoorayprojectapi.domain.tag.service;

import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.exception.ProjectNotFoundException;
import com.nhnacademy.minidoorayprojectapi.domain.project.service.ProjectService;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dao.TagRepository;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.request.TagCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.request.TagUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.response.TagSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.response.TagSeqNameDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.entity.Tag;
import com.nhnacademy.minidoorayprojectapi.domain.tag.exception.TagNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagService {
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;

    /**
     * 태그 전체 조회
     * @param projectSeq
     * @return
     */
    public List<TagSeqNameDto> getTagsByProjectSeq(Long projectSeq){
        return convertToTagSeqNameDtoList(tagRepository.findByProject_ProjectSeq(projectSeq));
    }

    private List<TagSeqNameDto> convertToTagSeqNameDtoList(List<Tag> tagList){
        return tagList.stream()
                .map(tag -> TagSeqNameDto.builder()
                        .tagSeq(tag.getTagSeq())
                        .tagName(tag.getTagName())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public TagSeqDto createTag(Long projectSeq, TagCreateRequestDto tagCreateRequest) {
        Tag newTag = Tag.builder()
                .tagName(tagCreateRequest.getTagName())
                .project(projectRepository.findById(projectSeq).orElseThrow(ProjectNotFoundException::new))
                .build();
        return convertToTagSeqDto(tagRepository.save(newTag));
    }

    public TagSeqDto convertToTagSeqDto(Tag tag){
        return TagSeqDto.builder()
                .tagSeq(tag.getTagSeq())
                .build();
    }

    /**
     *
     * @param tagSeq
     * @param tagUpdateRequestDto
     * @return
     */
    @Transactional
    public TagSeqDto updateTag(Long projectSeq, Long tagSeq ,TagUpdateRequestDto tagUpdateRequestDto){
        Tag tag = tagRepository.findByProject_ProjectSeqAndTagSeq(projectSeq, tagSeq).orElseThrow(TagNotFoundException::new);
        tag.updateTag(tagUpdateRequestDto.getTagName());
        return convertToTagSeqDto(tag);
    }

    @Transactional
    public void deleteTag(Long tagSeq){
        tagRepository.deleteById(tagSeq);
    }



}
