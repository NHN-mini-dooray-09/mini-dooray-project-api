package com.nhnacademy.minidoorayprojectapi.domain.comment.dao;

import com.nhnacademy.minidoorayprojectapi.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Page<Comment> getAllByTask_TaskSeq(Long taskSeq, Pageable pageable);
    Optional<Comment> findByTask_Project_ProjectSeqAndTask_TaskSeqAndCommentSeq
            (Long projectSeq, Long taskSeq, Long commentSeq);

    boolean existsByCommentSeqAndMemberSeq(Long commentSeq, Long memberSeq);


}
