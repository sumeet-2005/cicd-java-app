package com.cicd.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CicdJavaAppApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(CicdJavaAppApplication.class);
    
    public static void main(String[] args) {
        SpringApplication.run(CicdJavaAppApplication.class, args);
        logger.info("Application started successfully!");
        // Remove any System.out.println lines
    }
}
