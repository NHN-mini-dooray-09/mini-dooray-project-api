package com.nhnacademy.minidoorayprojectapi.domain.tag.service;

import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dao.TagRepository;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.request.TagCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.request.TagUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.response.TagSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.response.TagSeqNameDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.entity.Tag;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TagServiceTest {
    @InjectMocks
    TagService tagService;
    @Mock
    TagRepository tagRepository;
    @Mock
    ProjectRepository projectRepository;

    @Test
    @DisplayName("태그 전체 조회")
    void testGetTagsByProjectSeq() {

        List<Tag> tags = new ArrayList<>(
                Arrays.asList(Tag.builder().build(),Tag.builder().build())
        );

        when(tagRepository.findByProject_ProjectSeq(any()))
                .thenReturn(tags);

        List<TagSeqNameDto> result = tagService.getTagsByProjectSeq(any());

        verify(tagRepository, times(1)).findByProject_ProjectSeq(any());
        assertEquals(2,result.size());
    }

    @Test
    @DisplayName("태크 생성")
    void testCreateTag() {
        TagCreateRequestDto tagCreateRequestDto = new TagCreateRequestDto("tag1");

        when(projectRepository.findById(any()))
                .thenReturn(Optional.of(mock(Project.class)));

        when(tagRepository.save(any()))
                .thenReturn(mock(Tag.class));

        tagService.createTag(any(),tagCreateRequestDto);

        verify(tagRepository, times(1))
                .save(any());
    }


    @Test
    @DisplayName("태그 수정")
    void testUpdateTag() {
        Tag tag = Tag.builder()
                .tagName("tag")
                .build();

        when(tagRepository.findByProject_ProjectSeqAndTagSeq(anyLong(),anyLong()))
                .thenReturn(Optional.ofNullable(tag));

        TagUpdateRequestDto tagUpdateRequest = new TagUpdateRequestDto("tag-update");

        TagSeqDto result = tagService.updateTag(anyLong(),anyLong(),tagUpdateRequest);

        assertNotNull(result);
        assertEquals(tag.getTagName(),tagUpdateRequest.getTagName());

    }

    @Test
    @DisplayName("태그 삭제")
    void testDeleteTag() {

        tagService.deleteTag(any());

        verify(tagRepository, times(1))
                .deleteById(any());

    }
}