package com.nhnacademy.minidoorayprojectapi.domain.milestone.api;

import com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.request.MilestoneCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.request.MilestoneUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.response.MilestoneDto;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.response.MilestoneSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.service.MilestoneService;
import com.nhnacademy.minidoorayprojectapi.global.exception.ValidationFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class MilestoneApiController {
    private final MilestoneService milestoneService;

    public MilestoneApiController(MilestoneService milestoneService) {
        this.milestoneService = milestoneService;
    }

    //TODO 프로젝트 권한(member,admin) 있는놈만 마일스톤 만들 수 있음
    //TODO 인증 따로 만들어야 할 듯
    @GetMapping("/{project-seq}/milestones")
    public ResponseEntity<List<MilestoneDto>> getMilestones(@PathVariable("project-seq")Long projectSeq){
        return ResponseEntity.ok().body(milestoneService.getMilestonesByProjectSeq(projectSeq));
    }

    @PostMapping("/{project-seq}/milestones")
    public ResponseEntity<MilestoneSeqDto> createMilestone(@PathVariable("project-seq")Long projectSeq ,
                                                           @Valid @RequestBody MilestoneCreateRequestDto milestoneCreateRequest,
                                                           BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.created(URI.create("/project/"+projectSeq))
                .body(milestoneService.createMilestone(projectSeq,milestoneCreateRequest));
    }

    @PatchMapping("/{project-seq}/milestones/{milestone-seq}")
    public ResponseEntity<MilestoneSeqDto> updateMilestone(@PathVariable("project-seq")Long projectSeq ,
                                                           @PathVariable("milestone-seq") Long milestoneSeq,
                                                           @Valid @RequestBody MilestoneUpdateRequestDto milestoneUpdateRequest,
                                                           BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.ok().body(milestoneService.updateMilestone(projectSeq, milestoneSeq, milestoneUpdateRequest));
    }

    @DeleteMapping("/{project-seq}/milestones/{milestone-seq}")
    public ResponseEntity<Void> deleteMilestone(@PathVariable("project-seq")Long projectSeq ,
                                                @PathVariable("milestone-seq") Long milestoneSeq){
        milestoneService.deleteMilestone(milestoneSeq);
        return ResponseEntity.noContent()
                .build();
    }
}
