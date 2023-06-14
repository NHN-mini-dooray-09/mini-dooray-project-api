package com.nhnacademy.minidoorayprojectapi.domain.task.dto.response;

import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.response.CommentDto;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.response.MilestoneDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.response.TagSeqNameDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class TaskDto {
    private Long taskSeq;
    private Long memberSeq;
    private String taskTitle;
    private String taskContent;
    private String taskStatus;
    private LocalDateTime taskCreatedAt;
    private MilestoneDto milestone;
    private List<TagSeqNameDto> tags;
    private Page<CommentDto> comments;

    @Builder
    public TaskDto(Long taskSeq, Long memberSeq, String taskTitle, String taskContent, String taskStatus,
                   LocalDateTime taskCreatedAt, MilestoneDto milestone, List<TagSeqNameDto> tags, Page<CommentDto> comments) {
        this.taskSeq = taskSeq;
        this.memberSeq = memberSeq;
        this.taskTitle = taskTitle;
        this.taskContent = taskContent;
        this.taskStatus = taskStatus;
        this.taskCreatedAt = taskCreatedAt;
        this.milestone = milestone;
        this.tags = tags;
        this.comments = comments;
    }
}
