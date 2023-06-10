package com.nhnacademy.minidoorayprojectapi.domain.project.exception;

public class ProjectNotFoundException extends RuntimeException{
    public ProjectNotFoundException() {
        super("project가 존재하지 않습니다.");
    }
}
