package com.nhnacademy.minidoorayprojectapi.domain.comment.entity;

import com.nhnacademy.minidoorayprojectapi.domain.task.entity.Task;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(name = "comments")
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentSeq;
    @MapsId
    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;
    private Long memberSeq;
    private String commentContent;
    private LocalDateTime commentCreatedAt;

    @Builder
    public Comment(Task task, Long memberSeq, String commentContent) {
        this.task = task;
        this.memberSeq = memberSeq;
        this.commentContent = commentContent;
        this.commentCreatedAt = LocalDateTime.now();
    }

    public void updateComment(String commentContent){
        this.commentContent = commentContent;
    }
}
