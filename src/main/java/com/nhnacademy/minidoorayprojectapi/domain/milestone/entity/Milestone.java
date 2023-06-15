package com.nhnacademy.minidoorayprojectapi.domain.milestone.entity;

import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
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

    @Builder
    public Milestone(String milestoneName, LocalDateTime milestoneStartDate, LocalDateTime milestoneEndDate, Project project) {
        this.milestoneName = milestoneName;
        this.milestoneStartDate = milestoneStartDate;
        this.milestoneEndDate = milestoneEndDate;
        this.project = project;
    }

    public Long updateMilestone(String milestoneName, LocalDateTime milestoneStartDate, LocalDateTime milestoneEndDate){
        this.milestoneName= milestoneName;
        this.milestoneStartDate=milestoneStartDate;
        this.milestoneEndDate = milestoneEndDate;

        return milestoneSeq;
    }
}
