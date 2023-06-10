package com.nhnacademy.minidoorayprojectapi.global.config;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonRestControllerAdvice {
    @InitBinder
    public void dataBinder(WebDataBinder dataBinder){
        dataBinder.initDirectFieldAccess();
    }
}
