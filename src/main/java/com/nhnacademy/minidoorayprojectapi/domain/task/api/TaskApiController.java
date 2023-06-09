package com.nhnacademy.minidoorayprojectapi.domain.task.api;

import com.nhnacademy.minidoorayprojectapi.domain.task.dto.request.TaskCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.request.TaskUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskSeqNameAndMemberSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/projects")
public class TaskApiController {
    private final TaskService taskService;

    public TaskApiController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{project-seq}/task")
    public ResponseEntity<Page<TaskSeqNameAndMemberSeqDto>> getTasksInProject(@PageableDefault(size = 10, sort="taskSeq") Pageable pageable,
                                                                              @PathVariable("project-seq") Long projectSeq){
        return ResponseEntity.ok().body(taskService.getTasks(pageable, projectSeq));
    }


    @PostMapping("/{project-seq}")
    public ResponseEntity<TaskSeqDto> createTask(@PathVariable("project-seq") Long projectSeq,
                                                 @RequestParam("member-seq") Long memberSeq,
                                                 @RequestBody TaskCreateRequestDto taskCreateRequest){
        TaskSeqDto taskSeq = taskService.createTask(projectSeq, memberSeq, taskCreateRequest);
        return ResponseEntity.created(URI.create("/projects/"+projectSeq+"/"+taskSeq.getTaskSeq()))
                .body(taskSeq);
    }

    @GetMapping("/{project-seq}/{task-seq}")
    public ResponseEntity<TaskDto> getTask(@PathVariable("project-seq") Long projectSeq,
                                           @PathVariable("task-seq") Long taskSeq){
        return ResponseEntity.ok()
                .body(taskService.getTask(projectSeq, taskSeq));
    }

    @PatchMapping("/{project-seq}/{task-seq}")
    public ResponseEntity<TaskSeqDto> updateTask(@PathVariable("project-seq") Long projectSeq,
                                                 @PathVariable("task-seq") Long taskSeq,
                                                 @RequestBody TaskUpdateRequestDto taskUpdateRequest){
        return ResponseEntity.ok()
                .body(taskService.updateTask(projectSeq, taskSeq, taskUpdateRequest));
    }

    @DeleteMapping("/{project-seq}/{task-seq}")
    public ResponseEntity<Void> deleteTask(@PathVariable("project-seq") Long projectSeq,
                                           @PathVariable("task-seq") Long taskSeq){
        taskService.deleteTask(taskSeq);
        return ResponseEntity.noContent()
                .build();
    }








}
