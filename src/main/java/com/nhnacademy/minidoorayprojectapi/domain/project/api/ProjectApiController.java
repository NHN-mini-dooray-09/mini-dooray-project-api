package com.nhnacademy.minidoorayprojectapi.domain.project.api;

import com.nhnacademy.minidoorayprojectapi.domain.project.dto.request.ProjectAuthoritiesResisterRequestDto;
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

import javax.servlet.http.HttpSession;
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
    public ResponseEntity<ProjectSeqDto> createProject(@RequestBody ProjectCreateRequestDto projectRequest,
//                                                       HttpSession session,
                                                       @RequestParam("member-seq")Long memberSeq){
//        ProjectSeqDto seq = projectService.createProject(projectRequest,(Long) session.getAttribute("memberSeq"));
        ProjectSeqDto seq = projectService.createProject(projectRequest,memberSeq);
        return ResponseEntity.created(URI.create("/project/"+seq)).body(seq);
    }

    @GetMapping("/{project-seq}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable("project-seq") Long projectSeq,
//                                                 HttpSession session,
                                                 @RequestParam("member-seq")Long memberSeq){
        ProjectDto projectDto = projectService.getProject(projectSeq,memberSeq);
        return ResponseEntity.ok().body(projectDto);
    }

    @PatchMapping("/{project-seq}")
    public ResponseEntity<ProjectSeqDto> updateProject(@PathVariable("project-seq") Long projectSeq,
                                                       @RequestBody ProjectUpdateRequestDto projectRequest,
//                                                       HttpSession session,
                                                       @RequestParam("member-seq")Long memberSeq){
//        ProjectSeqDto seq = projectService.updateProject(projectSeq,
//                projectRequest,
//                (Long) session.getAttribute("memberSeq"));
        ProjectSeqDto seq = projectService.updateProject(projectSeq,
                projectRequest,
                memberSeq);
        return ResponseEntity.created(URI.create("/project/"+seq)).body(seq);
    }

    @PostMapping("/{project-seq}/members")
    public ResponseEntity<Void> resisterProjectMember(@PathVariable("project-seq") Long projectSeq,
                                                      @RequestBody ProjectAuthoritiesResisterRequestDto projectAuthoritiesResisterRequest,
//                                                      HttpSession session,
                                                      @RequestParam("member-seq")Long memberSeq){
//        projectService.resisterProjectMembers(projectSeq,
//                (Long) session.getAttribute("memberSeq"),
//                projectAuthoritiesResisterRequest);
        projectService.resisterProjectMembers(projectSeq,
                memberSeq,
                projectAuthoritiesResisterRequest);
        return ResponseEntity.ok().build();
    }




}
