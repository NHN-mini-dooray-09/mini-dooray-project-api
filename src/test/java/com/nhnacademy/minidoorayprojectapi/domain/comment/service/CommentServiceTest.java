package com.nhnacademy.minidoorayprojectapi.domain.comment.service;

import com.nhnacademy.minidoorayprojectapi.domain.comment.dao.CommentRepository;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.request.CommentCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.request.CommentUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.entity.Comment;
import com.nhnacademy.minidoorayprojectapi.domain.task.dao.TaskRepository;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.Task;
import com.nhnacademy.minidoorayprojectapi.global.exception.AccessDeniedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    CommentService commentService;
    @Mock
    CommentRepository commentRepository;
    @Mock
    TaskRepository taskRepository;

    @Test
    void getComments() {
        List<Comment> comments = new ArrayList<>(
                Arrays.asList(Comment.builder().build(),
                        Comment.builder().build(),
                        Comment.builder().build())
        );

        Pageable pageable = PageRequest.of(0,3);
        Page<Comment> commentPage = new PageImpl<>(comments, pageable, comments.size());

        when(commentRepository.getAllByTask_TaskSeq(any(),any()))
                .thenReturn(commentPage);

        commentService.getComments(1L,1L,pageable);

        verify(commentRepository, times(1))
                .getAllByTask_TaskSeq(any(),any());
    }

    @Test
    void testCreateComment() {
        when(taskRepository.findByProject_ProjectSeqAndTaskSeq(any(),any()))
                .thenReturn(Optional.of(mock(Task.class)));

        when(commentRepository.save(any()))
                .thenReturn(mock(Comment.class));

        CommentCreateRequestDto commentCreateRequest = new CommentCreateRequestDto();

        commentService.createComment(1L,1L,1L, commentCreateRequest);

        verify(taskRepository, times(1))
                .findByProject_ProjectSeqAndTaskSeq(any(),any());
        verify(commentRepository, times(1))
                .save(any());

    }

    @Test
    void updateComment() {
        Comment comment = Comment.builder().build();

        CommentUpdateRequestDto commentUpdateRequest = new CommentUpdateRequestDto("comment-update");

        when(commentRepository.existsByCommentSeqAndMemberSeq(any(),any()))
                .thenReturn(true);

        when(commentRepository.findByTask_Project_ProjectSeqAndTask_TaskSeqAndCommentSeq(any(),any(),any()))
                .thenReturn(Optional.ofNullable(comment));

        commentService.updateComment(1L,1L,1L,1L, commentUpdateRequest);

        verify(commentRepository, times(1))
                .existsByCommentSeqAndMemberSeq(any(),any());
        verify(commentRepository, times(1))
                .findByTask_Project_ProjectSeqAndTask_TaskSeqAndCommentSeq(any(),any(),any());


    }

    @Test
    @DisplayName("댓글 삭제- 작성자만 삭제 가능")
    void deleteComment() {

        when(commentRepository.existsByCommentSeqAndMemberSeq(any(),any()))
                .thenReturn(true);

        commentService.deleteComment(1L,1L,1L,1L);

        verify(commentRepository, times(1))
                .existsByCommentSeqAndMemberSeq(any(),any());
        verify(commentRepository, times(1))
                .deleteById(any());
    }

    @Test
    void testUpdateCommentUnauthorizedAccessException(){
        when(commentRepository.existsByCommentSeqAndMemberSeq(any(),any()))
                .thenReturn(false);

        CommentUpdateRequestDto commentUpdateRequest = new CommentUpdateRequestDto("comment-update");

        assertThrows(AccessDeniedException.class,
                () -> commentService.updateComment(1L,1L,1L,1L, commentUpdateRequest));
    }

    @Test
    void testDeleteCommentUnauthorizedAccessException() {

        when(commentRepository.existsByCommentSeqAndMemberSeq(any(),any()))
                .thenReturn(false);

        assertThrows(AccessDeniedException.class,
                () -> commentService.deleteComment(1L,1L,1L,1L));

        verify(commentRepository, times(1))
                .existsByCommentSeqAndMemberSeq(any(),any());
    }
}