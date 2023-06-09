package com.nhnacademy.minidoorayprojectapi.domain.project.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Table(name = "project_authorities")
@Entity
public class ProjectAuthority {
    @EmbeddedId
    private ProjectAuthoritiesPk projectAuthoritiesPk;
    @Column(name = "project_authority")
    private String authority;
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
    public ProjectAuthority(ProjectAuthoritiesPk projectAuthoritiesPk, String authority, Project project) {
        this.projectAuthoritiesPk = projectAuthoritiesPk;
        this.authority = authority;
        this.project = project;
    }
}
