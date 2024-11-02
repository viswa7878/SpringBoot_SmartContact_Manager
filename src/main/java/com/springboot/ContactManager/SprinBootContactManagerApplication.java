package com.springboot.ContactManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.springboot.dao", "com.springboot.service","com.springboot.ContactManager","com.springboot.Config"})
@EntityScan(basePackages = {"com.springboot.ContactManager.entities"})  // To scan your entity classes
@EnableJpaRepositories(basePackages = {"com.springboot.dao"})  // To scan your repository interfaces
public class SprinBootContactManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SprinBootContactManagerApplication.class, args);
    }  
}

    