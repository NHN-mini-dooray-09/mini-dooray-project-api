package com.nhnacademy.minidoorayprojectapi.domain.task.entity;

import com.nhnacademy.minidoorayprojectapi.domain.tag.entity.Tag;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Table(name = "task_tag")
@Entity
public class TaskTag {
    @EmbeddedId
    private TaskTagPk taskTagPk;

    @MapsId("taskSeq")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Task task;
    @MapsId("tagSeq")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Tag tag;


    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class TaskTagPk implements Serializable{
        private Long taskSeq;
        private Long tagSeq;
    }

    @Builder
    public TaskTag(TaskTagPk taskTagPk, Task task, Tag tag) {
        this.taskTagPk = taskTagPk;
        this.task = task;
        this.tag = tag;
    }
}
