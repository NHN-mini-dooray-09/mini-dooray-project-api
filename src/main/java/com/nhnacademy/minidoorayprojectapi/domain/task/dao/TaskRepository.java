package com.nhnacademy.minidoorayprojectapi.domain.task.dao;

import com.nhnacademy.minidoorayprojectapi.domain.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {
    Page<Task> getAllByProject_ProjectSeq(Pageable pageable, Long projectSeq);

    Optional<Task> findByProject_ProjectSeqAndTaskSeq(Long projectSeq, Long taskSeq);

    boolean existsByTaskSeqAndAndMemberSeq(Long taskSeq, Long memberSeq);
}
