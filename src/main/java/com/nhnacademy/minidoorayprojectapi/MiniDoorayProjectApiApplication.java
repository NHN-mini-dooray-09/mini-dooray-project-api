package com.nhnacademy.minidoorayprojectapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class MiniDoorayProjectApiApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MiniDoorayProjectApiApplication.class);
        application.run(args);
    }
}
