package com.nhnacademy.minidoorayprojectapi.domain.task.entity;

import com.nhnacademy.minidoorayprojectapi.domain.milestone.entity.Milestone;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.domain.tag.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(mappedBy = "task")
    private List<TaskTag> taskTags;


    @Builder
    public Task(Long memberSeq, Project project, Milestone milestone, String taskTitle,
                String taskContent, String taskStatus) {
        this.memberSeq = memberSeq;
        this.project = project;
        this.milestone = milestone;
        this.taskTitle = taskTitle;
        this.taskContent = taskContent;
        this.taskStatus = taskStatus;
        this.taskCreatedAt = LocalDateTime.now();
    }

    public void updateTask(String taskTitle, String taskContent, String taskStatus, Milestone milestone) {
        this.taskTitle = taskTitle;
        this.taskContent = taskContent;
        this.taskStatus = taskStatus;
        this.milestone = milestone;
    }

}
