package com.nhnacademy.minidoorayprojectapi.domain.tag.api;

import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.request.TagCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.request.TagUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.response.TagSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.response.TagSeqNameDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class TagApiController {
    private final TagService tagService;

    public TagApiController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/{project-seq}/tags")
    public ResponseEntity<List<TagSeqNameDto>> getTags(@PathVariable("project-seq") Long projectSeq) {
        return ResponseEntity.ok()
                .body(tagService.getTagsByProjectSeq(projectSeq));
    }

    @PostMapping("/{project-seq}/tags")
    public ResponseEntity<TagSeqDto> createTag(@PathVariable("project-seq") Long projectSeq,
                                               @RequestBody TagCreateRequestDto tagCreateRequest){
        return ResponseEntity.created(URI.create("/projects/"+projectSeq))
                .body(tagService.createTag(projectSeq, tagCreateRequest));
    }

    @PatchMapping("/{project-seq}/tags/{tag-seq}")
    public ResponseEntity<TagSeqDto> updateTag(@PathVariable("project-seq") Long projectSeq,
                                               @PathVariable("tag-seq") Long tagSeq,
                                               @RequestBody TagUpdateRequestDto tagUpdateRequest){
        return ResponseEntity.ok()
                .body(tagService.updateTag(projectSeq, tagSeq ,tagUpdateRequest));
    }

    @DeleteMapping("/{project-seq}/tags/{tag-seq}")
    public ResponseEntity<Void> createTag(@PathVariable("project-seq") Long projectSeq,
                                               @PathVariable("tag-seq") Long tagSeq){
        tagService.deleteTag(tagSeq);
        return ResponseEntity.noContent()
                .build();
    }



}
