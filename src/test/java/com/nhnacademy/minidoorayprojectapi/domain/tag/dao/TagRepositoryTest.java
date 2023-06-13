package com.nhnacademy.minidoorayprojectapi.domain.tag.dao;

import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.domain.tag.entity.Tag;
import com.nhnacademy.minidoorayprojectapi.domain.task.dao.TaskTagRepository;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.TaskTag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TagRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    TaskTagRepository taskTagRepository;

    Project project1;

    @BeforeEach
    void setUp(){
        project1 = Project.builder()
                .memberSeq(1L)
                .projectName("project1")
                .projectDescription("test project1")
                .projectStatus("할일")
                .build();
        projectRepository.save(project1);
    }

    @Test
    void testSaveTag(){
        Tag tag1 = Tag.builder()
                .build();
        tagRepository.save(tag1);

        Tag actual = tagRepository.findById(tag1.getTagSeq())
                .orElse(null);

        assertThat(actual.getTagSeq()).isEqualTo(tag1.getTagSeq());
    }

    @Test
    void testUpdateTag(){
        Tag tag2 = Tag.builder()
                .build();
        tagRepository.save(tag2);

        Tag tag3 = tagRepository.findById(tag2.getTagSeq())
                .orElse(null);
        tag3.updateTag("tag-change");

        assertThat(tag2.getTagName()).isEqualTo(tag3.getTagName());

    }
    @Test
    void testDeleteTag(){
        Tag tag4 = Tag.builder()
                .build();
        tagRepository.save(tag4);
        tagRepository.deleteById(tag4.getTagSeq());
        Tag actual =  tagRepository.findById(tag4.getTagSeq())
                .orElse(null);
        assertNull(actual);
    }

    @Test
    void findByProject_ProjectSeq() {
        Tag tag5 = Tag.builder()
                .project(project1)
                .build();

        Tag tag6 = Tag.builder()
                .project(project1)
                .build();

        tagRepository.save(tag5);
        tagRepository.save(tag6);

        List<Tag> actual = tagRepository.findByProject_ProjectSeq(project1.getProjectSeq());

        assertThat(actual).hasSize(2);
        assertThat(actual).extracting(Tag::getTagSeq)
                .containsExactlyInAnyOrder(tag5.getTagSeq(),tag6.getTagSeq());
    }

    @Test
    void findByProject_ProjectSeqAndTagSeq() {
        Tag tag7 = Tag.builder()
                .project(project1)
                .build();
        tagRepository.save(tag7);

        Tag actual = tagRepository.findByProject_ProjectSeqAndTagSeq(project1.getProjectSeq(),tag7.getTagSeq())
                .orElse(null);

        assertThat(actual).isNotNull();

    }
}