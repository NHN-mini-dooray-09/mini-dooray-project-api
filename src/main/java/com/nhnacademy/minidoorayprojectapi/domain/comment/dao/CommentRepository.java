package com.nhnacademy.minidoorayprojectapi.domain.comment.dao;

import com.nhnacademy.minidoorayprojectapi.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
