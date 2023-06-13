package com.nhnacademy.minidoorayprojectapi.domain.tag.entity;

import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.TaskTag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "tags")
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagSeq;
    private String tagName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Project project;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.REMOVE)
    private List<TaskTag> taskTags;

    @Builder
    public Tag(String tagName, Project project) {
        this.tagName = tagName;
        this.project = project;
    }

    public void updateTag(String tagName){
        this.tagName = tagName;
    }
}
