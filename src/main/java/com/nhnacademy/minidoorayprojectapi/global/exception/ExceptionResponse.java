package com.nhnacademy.minidoorayprojectapi.global.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExceptionResponse {

    private String message;
    private Integer status;
    private LocalDateTime timestamp;

    public ExceptionResponse(String message, Integer status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
