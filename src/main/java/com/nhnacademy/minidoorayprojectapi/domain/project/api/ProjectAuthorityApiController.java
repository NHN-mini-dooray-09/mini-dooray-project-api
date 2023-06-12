package com.nhnacademy.minidoorayprojectapi.domain.project.api;

import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectAuthorityDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.service.ProjectAuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProjectAuthorityApiController {

    private final ProjectAuthorityService projectAuthorityService;

    @GetMapping("/projects/{project-seq}/authorize")
    public ResponseEntity<ProjectAuthorityDto> getProjectAuthority(@PathVariable("project-seq") Long projectSeq,
                                                                   @RequestParam("member-seq") Long memberSeq){
        return ResponseEntity.ok()
                .body(projectAuthorityService.getProjectAuthority(projectSeq,memberSeq));
    }

}
