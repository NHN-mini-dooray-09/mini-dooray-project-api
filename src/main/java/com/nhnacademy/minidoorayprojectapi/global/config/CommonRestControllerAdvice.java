package com.nhnacademy.minidoorayprojectapi.global.config;

import com.nhnacademy.minidoorayprojectapi.global.exception.ExceptionResponse;
import com.nhnacademy.minidoorayprojectapi.global.exception.ProjectNotFoundException;
import com.nhnacademy.minidoorayprojectapi.global.exception.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonRestControllerAdvice {
    @InitBinder
    public void dataBinder(WebDataBinder dataBinder){
        dataBinder.initDirectFieldAccess();
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> notFoundError(RuntimeException exception){
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(exceptionResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ExceptionResponse> unauthorizedAccessError(RuntimeException exception){
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(exceptionResponse,HttpStatus.UNAUTHORIZED);
    }










}
