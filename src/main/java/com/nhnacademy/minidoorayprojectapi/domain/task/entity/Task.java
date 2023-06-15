package com.nhnacademy.minidoorayprojectapi.domain.task.entity;

import com.nhnacademy.minidoorayprojectapi.domain.comment.entity.Comment;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.entity.Milestone;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Table(name = "tasks")
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskSeq;
    private Long memberSeq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Project project;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Milestone milestone;
    private String taskTitle;
    private String taskContent;
    private String taskStatus;
    private LocalDateTime taskCreatedAt;

    @OneToMany(mappedBy = "task", cascade =CascadeType.REMOVE)
    private List<TaskTag> taskTags;

    @OneToMany(mappedBy = "task",cascade = CascadeType.REMOVE)
    private List<Comment> comments;


    @Builder
    public Task(Long memberSeq, Project project, Milestone milestone, String taskTitle,
                String taskContent, String taskStatus, List<TaskTag> taskTags) {
        this.memberSeq = memberSeq;
        this.project = project;
        this.milestone = milestone;
        this.taskTitle = taskTitle;
        this.taskContent = taskContent;
        this.taskStatus = taskStatus;
        this.taskCreatedAt = LocalDateTime.now();
        this.taskTags = Objects.isNull(taskTags) ? new ArrayList<>() : taskTags ;
        this.comments = new ArrayList<>();
    }

    public void updateTask(String taskTitle, String taskContent, String taskStatus, Milestone milestone,
                           List<TaskTag> taskTags) {
        this.taskTitle = taskTitle;
        this.taskContent = taskContent;
        this.taskStatus = taskStatus;
        this.milestone = milestone;
        this.taskTags = taskTags;
    }

}
