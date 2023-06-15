package com.nhnacademy.minidoorayprojectapi.domain.tag.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.request.TagCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.request.TagUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.response.TagSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.response.TagSeqNameDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagApiController.class)
class TagApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TagService tagService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetTags() throws Exception{

        List<TagSeqNameDto> tagSeqNameDtoList = Arrays.asList(
                new TagSeqNameDto(1L,"tag1"),
                new TagSeqNameDto(2L,"tag2")
        );

        when(tagService.getTagsByProjectSeq(any()))
                .thenReturn(tagSeqNameDtoList);

        mockMvc.perform(get("/projects/1/tags"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(status().isOk());

        verify(tagService, times(1))
                .getTagsByProjectSeq(any());
    }

    @Test
    void testCreateTag() throws Exception{
        TagCreateRequestDto tagCreateRequestDto = new TagCreateRequestDto("tag1");
        TagSeqDto tagSeqDto = new TagSeqDto(1L);

        when(tagService.createTag(any(),any()))
                .thenReturn(tagSeqDto);

        mockMvc.perform(post("/projects/1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagCreateRequestDto)))
                .andExpect(status().isCreated());

        verify(tagService, times(1))
                .createTag(any(),any());
    }

    @Test
    void testCreateTag_ValidationFailed() throws Exception{
        TagCreateRequestDto tagCreateRequestDto = new TagCreateRequestDto("");

        mockMvc.perform(post("/projects/1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagCreateRequestDto)))
                .andExpect(status().isBadRequest());

        verify(tagService, never())
                .createTag(any(),any());
    }

    @Test
    void testUpdateTag() throws Exception{
        TagUpdateRequestDto tagUpdateRequestDto = new TagUpdateRequestDto("tag-update");
        TagSeqDto tagSeqDto = new TagSeqDto(1L);

        when(tagService.updateTag(any(),any(),any()))
                .thenReturn(tagSeqDto);

        mockMvc.perform(patch("/projects/1/tags/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(tagUpdateRequestDto)))
                .andExpect(status().isOk());

        verify(tagService, times(1))
                .updateTag(any(),any(),any());
    }

    @Test
    void testUpdateTag_ValidationFailed() throws Exception{
        TagUpdateRequestDto tagUpdateRequestDto = new TagUpdateRequestDto("");

        mockMvc.perform(patch("/projects/1/tags/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagUpdateRequestDto)))
                .andExpect(status().isBadRequest());

        verify(tagService, never())
                .updateTag(any(),any(),any());
    }

    @Test
    void testDeleteTag() throws Exception{
        mockMvc.perform(delete("/projects/1/tags/1"))
                        .andExpect(status().isNoContent());

        verify(tagService, times(1))
                .deleteTag(any());
    }
}