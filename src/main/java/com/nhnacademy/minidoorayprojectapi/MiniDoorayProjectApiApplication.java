package com.nhnacademy.minidoorayprojectapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MiniDoorayProjectApiApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MiniDoorayProjectApiApplication.class);
        application.run(args);
    }
}
