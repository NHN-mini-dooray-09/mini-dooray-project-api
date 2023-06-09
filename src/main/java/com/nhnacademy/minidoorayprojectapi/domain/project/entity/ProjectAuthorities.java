package com.nhnacademy.minidoorayprojectapi.domain.project.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Table(name = "project_authorities")
@Entity
public class ProjectAuthorities {
    @EmbeddedId
    private ProjectAuthoritiesPk projectAuthoritiesPk;
    private String projectAuthority;
    @MapsId("projectSeq")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_seq")
    private Project project;

    @Embeddable
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class ProjectAuthoritiesPk implements Serializable {
        @Column(name = "project_seq")
        private Long projectSeq;
        private Long memberSeq;
    }

    @Builder
    public ProjectAuthorities(ProjectAuthoritiesPk projectAuthoritiesPk, String projectAuthority, Project project) {
        this.projectAuthoritiesPk = projectAuthoritiesPk;
        this.projectAuthority = projectAuthority;
        this.project = project;
    }
}
