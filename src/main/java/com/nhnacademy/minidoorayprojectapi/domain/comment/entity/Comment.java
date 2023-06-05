package com.nhnacademy.minidoorayprojectapi.domain.comment.entity;

import com.nhnacademy.minidoorayprojectapi.domain.task.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
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
}
