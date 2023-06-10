package com.nhnacademy.minidoorayprojectapi.domain.project.exception;

public class ProjectPermissionDeniedException extends RuntimeException{
    public ProjectPermissionDeniedException() {
        super("403 error 프로젝트 접근 권한이 없습니다.");
    }
}
