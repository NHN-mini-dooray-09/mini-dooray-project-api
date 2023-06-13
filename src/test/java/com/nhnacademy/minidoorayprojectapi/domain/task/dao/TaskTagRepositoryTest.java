package com.nhnacademy.minidoorayprojectapi.domain.task.dao;

import com.nhnacademy.minidoorayprojectapi.domain.tag.dao.TagRepository;
import com.nhnacademy.minidoorayprojectapi.domain.tag.entity.Tag;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.Task;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.TaskTag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaskTagRepositoryTest {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    TaskTagRepository taskTagRepository;

    Task task1;
    Tag tag1;

    @BeforeEach
    public void setUp(){
        task1 = Task.builder()
            .build();
        taskRepository.save(task1);

        tag1 = Tag.builder().build();
        tagRepository.save(tag1);
    }

    @Test
    void testSaveTaskTag(){

        TaskTag taskTag1 = TaskTag.builder()
                .taskTagPk(new TaskTag.TaskTagPk(task1.getTaskSeq(),tag1.getTagSeq()))
                .tag(tag1)
                .task(task1)
                .build();
        taskTagRepository.save(taskTag1);

        TaskTag expected = taskTagRepository.findById(new TaskTag.TaskTagPk(task1.getTaskSeq(), tag1.getTagSeq()))
                .orElse(null);

        assertNotNull(expected);

    }

    @Test
    void testFindAllByTask_TaskSeq() {

        Task task1 = Task.builder()
                .build();
        taskRepository.save(task1);

        Tag tag1 = Tag.builder().build();
        Tag tag2 = Tag.builder().build();
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        TaskTag taskTag1 = TaskTag.builder()
                .taskTagPk(new TaskTag.TaskTagPk(task1.getTaskSeq(),tag1.getTagSeq()))
                .tag(tag1)
                .task(task1)
                .build();
        taskTagRepository.save(taskTag1);
        List<TaskTag> actual = taskTagRepository.findAllByTask_TaskSeq(task1.getTaskSeq());

        assertThat(actual).hasSize(1);
    }
}