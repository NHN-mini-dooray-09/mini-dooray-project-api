package com.nhnacademy.minidoorayprojectapi.domain.tag.dao;

import com.nhnacademy.minidoorayprojectapi.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Long> {
}
