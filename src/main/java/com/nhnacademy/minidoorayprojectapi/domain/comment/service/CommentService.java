package com.nhnacademy.minidoorayprojectapi.domain.comment.service;

import com.nhnacademy.minidoorayprojectapi.domain.comment.dao.CommentRepository;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.request.CommentCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.request.CommentUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.response.CommentDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.response.CommentSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.entity.Comment;
import com.nhnacademy.minidoorayprojectapi.domain.comment.exception.CommentNotFoundException;
import com.nhnacademy.minidoorayprojectapi.domain.task.dao.TaskRepository;
import com.nhnacademy.minidoorayprojectapi.domain.task.exception.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;


    public Page<CommentDto> getComments(Long projectSeq, Long taskSeq, Pageable pageable){
        return convertToCommentDtoPage(commentRepository.getAllByTask_TaskSeq(taskSeq, pageable));
    }

    private Page<CommentDto> convertToCommentDtoPage(Page<Comment> comments) {
        List<CommentDto> commentDtoList = comments.stream()
                .map(comment -> new CommentDto(comment.getCommentSeq(),comment.getMemberSeq(),comment.getCommentContent(),comment.getCommentCreatedAt()))
                .collect(Collectors.toList());
        return new PageImpl<>(commentDtoList,comments.getPageable(),comments.getTotalElements());
    }

    @Transactional
    public CommentSeqDto createComment(Long projectSeq, Long taskSeq, Long memberSeq,
                                       CommentCreateRequestDto commentCreateRequest){
        Comment newComment = Comment.builder()
                .memberSeq(memberSeq)
                .task(taskRepository.findByProject_ProjectSeqAndTaskSeq(projectSeq, taskSeq)
                        .orElseThrow(TaskNotFoundException::new))
                .commentContent(commentCreateRequest.getCommentContent())
                .build();
        return convertToCommentSeqDto(commentRepository.save(newComment));
    }

    @Transactional
    public CommentSeqDto updateComment(Long projectSeq, Long taskSeq, Long memberSeq, Long commentSeq,
                                       CommentUpdateRequestDto commentUpdateRequestDto){
        Comment comment = commentRepository.findByTask_Project_ProjectSeqAndTask_TaskSeqAndCommentSeqAndMemberSeq
                        (projectSeq, taskSeq, commentSeq, memberSeq)
                .orElseThrow(CommentNotFoundException::new);
        comment.updateComment(commentUpdateRequestDto.getCommentContent());
        return convertToCommentSeqDto(comment);
    }

    private CommentSeqDto convertToCommentSeqDto(Comment comment) {
        return new CommentSeqDto(comment.getCommentSeq());
    }

    @Transactional
    public void deleteComment(Long projectSeq, Long taskSeq, Long commentSeq){
        commentRepository.deleteById(commentSeq);
    }

}
