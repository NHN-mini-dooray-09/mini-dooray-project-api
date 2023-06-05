package com.nhnacademy.minidoorayprojectapi.domain.milestone.entity;

import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "milestones")
@Entity
public class Milestone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long milestoneSeq;
    private String milestoneName;
    private LocalDateTime milestoneStartDate;
    private LocalDateTime milestoneEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Project project;
}
