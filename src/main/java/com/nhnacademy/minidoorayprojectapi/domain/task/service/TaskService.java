package com.nhnacademy.minidoorayprojectapi.domain.task.service;

import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.exception.ProjectNotFoundException;
import com.nhnacademy.minidoorayprojectapi.domain.task.dao.TaskRepository;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.TaskSeqNameAndMemberSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;


    /**
     * 특정 프로젝트의 업무 목록 조회
     * @param pageable
     * @param projectSeq
     * @return
     */
    public Page<TaskSeqNameAndMemberSeqDto> getTasks(Pageable pageable, Long projectSeq ){
        if(!projectRepository.existsById(projectSeq)){
            throw new ProjectNotFoundException();
        }

        Page<Task> taskPage = taskRepository.getAllByProject_ProjectSeq(pageable, projectSeq);
        return convertToTaskPageDto(taskPage);
    }

    private Page<TaskSeqNameAndMemberSeqDto> convertToTaskPageDto(Page<Task> taskPage){
        List<TaskSeqNameAndMemberSeqDto> taskPageDto = taskPage.getContent()
                .stream()
                .map(task -> new TaskSeqNameAndMemberSeqDto(task.getTaskSeq(),task.getTaskTitle() ,task.getMemberSeq()))
                .collect(Collectors.toList());
        return new PageImpl<>(taskPageDto,taskPage.getPageable(),taskPage.getTotalElements());
    }


}
