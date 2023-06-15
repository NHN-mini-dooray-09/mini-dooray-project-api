package com.nhnacademy.minidoorayprojectapi.domain.task.dao;

import com.nhnacademy.minidoorayprojectapi.domain.task.entity.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskTagRepository extends JpaRepository<TaskTag, TaskTag.TaskTagPk> {
    List<TaskTag> findAllByTask_TaskSeq(Long taskSeq);
}
