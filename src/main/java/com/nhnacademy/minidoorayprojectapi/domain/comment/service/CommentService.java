package com.nhnacademy.minidoorayprojectapi.domain.comment.service;

import com.nhnacademy.minidoorayprojectapi.domain.comment.dao.CommentRepository;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.request.CommentCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.request.CommentUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.response.CommentDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.response.CommentSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.entity.Comment;
import com.nhnacademy.minidoorayprojectapi.global.exception.ProjectNotFoundException;
import com.nhnacademy.minidoorayprojectapi.domain.task.dao.TaskRepository;
import com.nhnacademy.minidoorayprojectapi.global.exception.UnauthorizedAccessException;
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
                        .orElseThrow(() -> new ProjectNotFoundException("해당 업무를 찾을 수 없습니다.")))
                .commentContent(commentCreateRequest.getCommentContent())
                .build();
        return convertToCommentSeqDto(commentRepository.save(newComment));
    }

    @Transactional
    public CommentSeqDto updateComment(Long projectSeq, Long taskSeq, Long memberSeq, Long commentSeq,
                                       CommentUpdateRequestDto commentUpdateRequestDto){
        if(!commentRepository.existsByCommentSeqAndMemberSeq(commentSeq, memberSeq)){
            throw new UnauthorizedAccessException();
        }
        Comment comment = commentRepository.findByTask_Project_ProjectSeqAndTask_TaskSeqAndCommentSeq
                        (projectSeq, taskSeq, commentSeq)
                .orElseThrow(() -> new ProjectNotFoundException("해당 댓글을 찾을 수 없습니다."));
        comment.updateComment(commentUpdateRequestDto.getCommentContent());
        return convertToCommentSeqDto(comment);
    }

    private CommentSeqDto convertToCommentSeqDto(Comment comment) {
        return new CommentSeqDto(comment.getCommentSeq());
    }

    @Transactional
    public void deleteComment(Long projectSeq, Long taskSeq, Long memberSeq, Long commentSeq){
        if(!commentRepository.existsByCommentSeqAndMemberSeq(commentSeq, memberSeq)){
            throw new UnauthorizedAccessException();
        }
        commentRepository.deleteById(commentSeq);
    }

}
