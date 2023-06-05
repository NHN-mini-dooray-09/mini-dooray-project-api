package com.nhnacademy.minidoorayprojectapi.domain.project.entity;

import com.nhnacademy.minidoorayprojectapi.domain.milestone.entity.Milestone;
import com.nhnacademy.minidoorayprojectapi.domain.tag.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "projects")
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectSeq;
    private Long memberSeq;
    private String projectName;
    private String projectDescription;
    private String projectStatus;
    private LocalDateTime projectCreatedAt;
    @OneToMany(mappedBy = "project")
    private List<Tag> tagList;
    @OneToMany(mappedBy = "project")
    private List<Milestone> milestoneList;
}
