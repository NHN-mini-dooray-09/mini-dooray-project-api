package com.nhnacademy.minidoorayprojectapi.domain.tag.dao;

import com.nhnacademy.minidoorayprojectapi.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {
    List<Tag> findByProject_ProjectSeq(Long projectSeq);
    Optional<Tag> findByProject_ProjectSeqAndTagSeq(Long projectSeq, Long tagSeq);
}
