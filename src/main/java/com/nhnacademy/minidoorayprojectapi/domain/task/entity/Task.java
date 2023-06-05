package com.nhnacademy.minidoorayprojectapi.domain.task.entity;

import com.nhnacademy.minidoorayprojectapi.domain.milestone.entity.Milestone;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
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
}
