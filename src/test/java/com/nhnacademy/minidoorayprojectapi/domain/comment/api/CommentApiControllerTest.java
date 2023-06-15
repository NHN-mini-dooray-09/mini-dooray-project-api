package com.nhnacademy.minidoorayprojectapi.domain.comment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.request.CommentCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.request.CommentUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.response.CommentDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.response.CommentSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentApiController.class)
class CommentApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    CommentService commentService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getComments() throws Exception {
        List<CommentDto> commentDtoList = Arrays.asList(
          new CommentDto(1L,1L,"comment1", LocalDateTime.now()),
          new CommentDto(2L,2L,"comment2",LocalDateTime.now()),
          new CommentDto(3L,1L,"comment3",LocalDateTime.now())
        );
        Page<CommentDto> commentDtoPage = new PageImpl<>(commentDtoList);

        when(commentService.getComments(any(),any(),any()))
                .thenReturn(commentDtoPage);

        mockMvc.perform(get("/projects/1/1/comments/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(3)));

        verify(commentService, times(1))
                .getComments(any(),any(),any());


    }

    @Test
    void testCreateComment() throws Exception{
        CommentCreateRequestDto commentCreateRequestDto = new CommentCreateRequestDto("comment-create-test");

        CommentSeqDto commentSeqDto = new CommentSeqDto(1L);

        when(commentService.createComment(any(),any(),any(),any()))
                .thenReturn(commentSeqDto);

        mockMvc.perform(post("/projects/1/1/comments")
                        .param("member-seq","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreateRequestDto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(commentService, times(1))
                .createComment(any(),any(),any(),any());
    }

    @Test
    void testCreateComment_ValidationFailed() throws Exception{
        CommentCreateRequestDto commentCreateRequestDto = new CommentCreateRequestDto("");

        mockMvc.perform(post("/projects/1/1/comments")
                        .param("member-seq","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreateRequestDto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(commentService, never())
                .createComment(any(),any(),any(),any());
    }

    @Test
    void testUpdateComment() throws Exception{
        CommentUpdateRequestDto commentUpdateRequestDto = new CommentUpdateRequestDto("comment-update");

        CommentSeqDto commentSeqDto = new CommentSeqDto(1L);

        when(commentService.updateComment(any(),any(),any(),any(),any()))
                .thenReturn(commentSeqDto);

        mockMvc.perform(patch("/projects/1/1/comments/1")
                        .param("member-seq","1")
                        .content(objectMapper.writeValueAsString(commentUpdateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(commentService, times(1))
                .updateComment(any(),any(),any(),any(),any());

    }

    @Test
    void testUpdateComment_ValidationFailed() throws Exception{
        CommentUpdateRequestDto commentUpdateRequestDto = new CommentUpdateRequestDto("");

        mockMvc.perform(patch("/projects/1/1/comments/1")
                        .param("member-seq","1")
                        .content(objectMapper.writeValueAsString(commentUpdateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(commentService, never())
                .updateComment(any(),any(),any(),any(),any());

    }

    @Test
    void deleteComment() throws Exception {

        mockMvc.perform(delete("/projects/1/1/comments/1")
                        .param("member-seq","1"))
                .andExpect(status().isNoContent());

        verify(commentService, times(1))
                .deleteComment(any(),any(),any(),any());
    }
}