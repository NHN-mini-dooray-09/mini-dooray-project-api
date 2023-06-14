package com.nhnacademy.minidoorayprojectapi.domain.comment.dao;

import com.nhnacademy.minidoorayprojectapi.domain.comment.entity.Comment;
import com.nhnacademy.minidoorayprojectapi.domain.project.entity.Project;
import com.nhnacademy.minidoorayprojectapi.domain.task.dao.TaskRepository;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;
    @Autowired
    CommentRepository commentRepository;

    Task task1;

    @BeforeEach
    public void setUp(){
        task1 = Task.builder().build();
        testEntityManager.persist(task1);
    }

    @Test
    void testUpdateComment(){
        Comment comment = Comment.builder()
                .task(task1)
                .build();
        commentRepository.save(comment);
        Comment comment2 = commentRepository.findById(comment.getCommentSeq()).orElse(null);

        comment2.updateComment("comment-change-test");

        assertEquals(comment.getCommentContent(),comment2.getCommentContent());

    }

    @Test
    void getAllByTask_TaskSeq() {
        Comment comment = Comment.builder()
                .task(task1)
                .memberSeq(1L)
                .build();
        commentRepository.save(comment);

        Pageable pageable = Pageable.ofSize(2);

        Page<Comment> expected = commentRepository.getAllByTask_TaskSeq(task1.getTaskSeq(), pageable);

        assertThat(expected.getContent()).hasSize(1);
    }

    @Test
    void findByTask_Project_ProjectSeqAndTask_TaskSeqAndCommentSeq() {
        Comment comment = Comment.builder()
                .task(task1)
                .memberSeq(1L)
                .build();
        commentRepository.save(comment);

        Comment expected = commentRepository.findByTask_Project_ProjectSeqAndTask_TaskSeqAndCommentSeq
                (null, task1.getTaskSeq(), comment.getCommentSeq())
                .orElse(null);

        assertNotNull(expected);

    }

    @Test
    void existsByCommentSeqAndMemberSeq() {
        Comment comment = Comment.builder()
                .task(task1)
                .memberSeq(1L)
                .build();
        commentRepository.save(comment);

        boolean excepted = commentRepository.existsByCommentSeqAndMemberSeq(comment.getCommentSeq(),1L);

        assertTrue(excepted);
    }
}