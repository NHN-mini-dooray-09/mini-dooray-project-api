package com.nhnacademy.minidoorayprojectapi.domain.project.entity;

import com.nhnacademy.minidoorayprojectapi.domain.milestone.entity.Milestone;
import com.nhnacademy.minidoorayprojectapi.domain.tag.entity.Tag;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "projects")
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_seq")
    private Long projectSeq;
    private Long memberSeq;
    private String projectName;
    private String projectDescription;
    private String projectStatus;
    private LocalDateTime projectCreatedAt;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Task> tasks;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Tag> tags;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Milestone> milestones;
    @OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ProjectAuthority> projectMembers;

    @Builder
    public Project(Long memberSeq, String projectName, String projectDescription, String projectStatus) {
        this.memberSeq = memberSeq;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectStatus = projectStatus;
        this.projectCreatedAt = LocalDateTime.now();
        this.tasks = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.milestones = new ArrayList<>();
        this.projectMembers = new ArrayList<>();
    }

    public void updateProject(String projectName, String projectStatus, String projectDescription){
        this.projectName = projectName;
        this.projectStatus = projectStatus;
        this.projectDescription = projectDescription;

    }




}
