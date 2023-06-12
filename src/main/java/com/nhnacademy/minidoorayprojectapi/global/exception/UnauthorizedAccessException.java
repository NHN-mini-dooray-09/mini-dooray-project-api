package com.nhnacademy.minidoorayprojectapi.global.exception;

public class UnauthorizedAccessException extends RuntimeException{

    public UnauthorizedAccessException(String message) {
        super(message + "접근 권한이 없습니다.");
    }

    public UnauthorizedAccessException() {
        super("접근 권한이 없습니다.");
    }
}
