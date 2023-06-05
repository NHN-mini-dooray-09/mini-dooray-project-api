package com.nhnacademy.minidoorayprojectapi.domain.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_authorities")
@Entity
public class ProjectAuthorities {
    @Id
    private Long projectSeq;

    private String projectAuthority;

    private Long memberSeq;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Project project;
}
