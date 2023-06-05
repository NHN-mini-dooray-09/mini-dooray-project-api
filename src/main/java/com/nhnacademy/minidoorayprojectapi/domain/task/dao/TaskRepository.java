package com.nhnacademy.minidoorayprojectapi.domain.task.dao;

import com.nhnacademy.minidoorayprojectapi.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
}
