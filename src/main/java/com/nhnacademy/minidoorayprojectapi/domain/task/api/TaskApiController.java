package com.nhnacademy.minidoorayprojectapi.domain.task.api;

import com.nhnacademy.minidoorayprojectapi.domain.task.dto.TaskSeqNameAndMemberSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskApiController {
    private final TaskService taskService;

    public TaskApiController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/projects/{project-seq}/task")
    public ResponseEntity<Page<TaskSeqNameAndMemberSeqDto>> getTasksInProject(@PageableDefault(size = 10, sort="taskSeq") Pageable pageable,
                                                                              @PathVariable("project-seq") Long projectSeq){
        return ResponseEntity.ok().body(taskService.getTasks(pageable, projectSeq));
    }








}
