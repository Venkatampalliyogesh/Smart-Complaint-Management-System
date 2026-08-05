package com.scms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.scms")
@EnableJpaRepositories(basePackages = "com.scms.repository")
@EntityScan(basePackages = "com.scms.entity")
public class SmartComplaintManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartComplaintManagementApplication.class, args);
    }
}
