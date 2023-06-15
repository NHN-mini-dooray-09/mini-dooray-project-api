package com.nhnacademy.minidoorayprojectapi.domain.project.api;

import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectAuthorityResisterRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectSeqNameDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.service.ProjectService;
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
public class ProjectApiController {
    private final ProjectService projectService;

    public ProjectApiController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ProjectSeqNameDto>> getProjectAll( @PageableDefault(size = 10, sort = "projectSeq")
                                                                      Pageable pageable,
                                                                  @RequestParam("member-seq") Long memberSeq) {
        return ResponseEntity.ok(projectService.getProjectAll(pageable, memberSeq));
    }

    @PostMapping
    public ResponseEntity<ProjectSeqDto> createProject(@Valid @RequestBody ProjectCreateRequestDto projectRequest,
                                                        BindingResult bindingResult,
                                                       @RequestParam("member-seq")Long memberSeq){
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }
        ProjectSeqDto seq = projectService.createProject(projectRequest,memberSeq);
        return ResponseEntity.created(URI.create("/project/"+seq)).body(seq);
    }

    @GetMapping("/{project-seq}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable("project-seq") Long projectSeq,
                                                 @RequestParam("member-seq")Long memberSeq){
        ProjectDto projectDto = projectService.getProject(projectSeq,memberSeq);
        return ResponseEntity.ok().body(projectDto);
    }

    @PatchMapping("/{project-seq}")
    public ResponseEntity<ProjectSeqDto> updateProject(@PathVariable("project-seq") Long projectSeq,
                                                       @Valid @RequestBody ProjectUpdateRequestDto projectRequest,
                                                        BindingResult bindingResult,
                                                       @RequestParam("member-seq")Long memberSeq){

        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }
        ProjectSeqDto seq = projectService.updateProject(projectSeq,
                projectRequest,
                memberSeq);
        return ResponseEntity.ok()
                .body(seq);
    }

    @PostMapping("/{project-seq}/members")
    public ResponseEntity<Void> resisterProjectMember(@PathVariable("project-seq") Long projectSeq,
                                                      @Valid @RequestBody ProjectAuthorityResisterRequestDto projectAuthoritiesResisterRequest,
                                                        BindingResult bindingResult,
                                                      @RequestParam("member-seq")Long memberSeq){
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }
        projectService.resisterProjectMembers(projectSeq,
                memberSeq,
                projectAuthoritiesResisterRequest);
        return ResponseEntity.ok().build();
    }




}
