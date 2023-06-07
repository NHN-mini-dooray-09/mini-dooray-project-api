package com.nhnacademy.minidoorayprojectapi.domain.project.api;

import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectSeqNameDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectApiController {
    private final ProjectService projectService;

    public ProjectApiController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ProjectSeqNameDto>> getProjectAll( @PageableDefault(size = 10, sort = "projectSeq")
                                                                      Pageable pageable) {
        return ResponseEntity.ok(projectService.getProjectAll(pageable));
    }

    //TODO GetMapping은 webconfig에서 처리한다.
    @PostMapping
    public ResponseEntity<ProjectSeqDto> createProject(@RequestBody ProjectCreateRequestDto projectRequest){
        ProjectSeqDto seq = projectService.createProject(projectRequest);
        return ResponseEntity.created(URI.create("/project/"+seq)).body(seq);
    }

    @GetMapping("/{project-seq}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable("project-seq") Long projectSeq){
        ProjectDto projectDto = projectService.getProject(projectSeq);
        return ResponseEntity.ok().body(projectDto);
    }

    @PatchMapping("/{project-seq}")
    public ResponseEntity<ProjectSeqDto> updateProject(@PathVariable("project-seq") Long projectSeq,
                                                       @RequestBody ProjectUpdateRequestDto projectRequest){
        ProjectSeqDto seq = projectService.updateProject(projectSeq, projectRequest);
        return ResponseEntity.created(URI.create("/project/"+seq)).body(seq);
    }




}
