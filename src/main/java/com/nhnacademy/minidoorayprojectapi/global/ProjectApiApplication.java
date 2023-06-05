package com.nhnacademy.minidoorayprojectapi.global;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectApiApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ProjectApiApplication.class);
        application.run(args);
    }
}
