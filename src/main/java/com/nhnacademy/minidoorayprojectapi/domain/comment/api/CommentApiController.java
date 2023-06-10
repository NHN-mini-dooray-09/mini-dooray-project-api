package com.nhnacademy.minidoorayprojectapi.domain.comment.api;

import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.request.CommentCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.request.CommentUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.response.CommentDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.response.CommentSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.service.CommentService;
import com.nhnacademy.minidoorayprojectapi.global.exception.ValidationFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class CommentApiController {

    private final CommentService commentService;

    @GetMapping("/{project-seq}/{task-seq}/comments/all")
    public ResponseEntity<Page<CommentDto>> getComments(@PathVariable("project-seq") Long projectSeq,
                                                        @PathVariable("task-seq") Long taskSeq,
                                                        @PageableDefault(size = 5, sort = "commentCreatedAt") Pageable pageable){
        return ResponseEntity.ok()
                .body(commentService.getComments(projectSeq,taskSeq,pageable));
    }

    @PostMapping("/{project-seq}/{task-seq}/comments")
    public ResponseEntity<CommentSeqDto> createComment(@PathVariable("project-seq") Long projectSeq,
                                                       @PathVariable("task-seq") Long taskSeq,
                                                       @RequestParam("member-seq")Long memberSeq,
                                                       @Valid @RequestBody CommentCreateRequestDto commentCreateRequest,
                                                       BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.created(URI.create("/project/"+projectSeq+"/"+taskSeq))
                .body(commentService.createComment(projectSeq, taskSeq, memberSeq, commentCreateRequest));
    }

    @PatchMapping("/{project-seq}/{task-seq}/comments/{comment-seq}")
    public ResponseEntity<CommentSeqDto> updateComment(@PathVariable("project-seq") Long projectSeq,
                                          @PathVariable("task-seq") Long taskSeq,
                                          @PathVariable("comment-seq") Long commentSeq,
                                          @RequestParam("member-seq")Long memberSeq,
                                          @Valid @RequestBody CommentUpdateRequestDto commentUpdateRequestDto,
                                          BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.ok()
                .body(commentService.updateComment(projectSeq, taskSeq, memberSeq, commentSeq, commentUpdateRequestDto));
    }

    @DeleteMapping("/{project-seq}/{task-seq}/comments/{comment-seq}")
    public ResponseEntity<Void> deleteComment(@PathVariable("project-seq") Long projectSeq,
                                              @PathVariable("task-seq") Long taskSeq,
                                              @PathVariable("comment-seq")Long commentSeq){

        commentService.deleteComment(projectSeq,taskSeq,commentSeq);
        return ResponseEntity.noContent().build();
    }


}
