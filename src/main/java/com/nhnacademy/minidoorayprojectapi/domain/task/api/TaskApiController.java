package com.nhnacademy.minidoorayprojectapi.domain.task.api;

import com.nhnacademy.minidoorayprojectapi.domain.task.dto.request.TaskCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.request.TaskUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskSeqNameAndMemberSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.service.TaskService;
import com.nhnacademy.minidoorayprojectapi.global.exception.ValidationFailedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
                                                 @Valid @RequestBody TaskCreateRequestDto taskCreateRequest,
                                                 BindingResult bindingResult){
        TaskSeqDto taskSeq = taskService.createTask(projectSeq, memberSeq, taskCreateRequest);
        return ResponseEntity.created(URI.create("/projects/"+projectSeq+"/"+taskSeq.getTaskSeq()))
                .body(taskSeq);
    }

    @GetMapping("/{project-seq}/{task-seq}")
    public ResponseEntity<TaskDto> getTask(@PathVariable("project-seq") Long projectSeq,
                                           @PathVariable("task-seq") Long taskSeq,
                                           @PageableDefault(size = 5, sort = "commentCreatedAt") Pageable pageable){
        return ResponseEntity.ok()
                .body(taskService.getTask(projectSeq, taskSeq, pageable));
    }

    @PatchMapping("/{project-seq}/{task-seq}")
    public ResponseEntity<TaskSeqDto> updateTask(@PathVariable("project-seq") Long projectSeq,
                                                 @PathVariable("task-seq") Long taskSeq,
                                                 @RequestParam("member-seq")Long memberSeq,
                                                 @Valid @RequestBody TaskUpdateRequestDto taskUpdateRequest,
                                                 BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.ok()
                .body(taskService.updateTask(projectSeq, taskSeq, memberSeq,taskUpdateRequest));
    }

    @DeleteMapping("/{project-seq}/{task-seq}")
    public ResponseEntity<Void> deleteTask(@PathVariable("project-seq") Long projectSeq,
                                           @PathVariable("task-seq") Long taskSeq,
                                           @RequestParam("member-seq")Long memberSeq){
        taskService.deleteTask(taskSeq, memberSeq);
        return ResponseEntity.noContent()
                .build();
    }








}
