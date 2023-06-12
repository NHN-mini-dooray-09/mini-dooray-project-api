package com.nhnacademy.minidoorayprojectapi.global.exception;

public class CommonNotFoundException extends RuntimeException{
    public CommonNotFoundException(String message) {
        super(message);
    }
}
