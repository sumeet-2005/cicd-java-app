package com.cicd.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CicdJavaAppApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CicdJavaAppApplication.class, args);
        System.out.println("🚀 CI/CD Java App is running!");
    }
}
