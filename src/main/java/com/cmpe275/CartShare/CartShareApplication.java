package com.cmpe275.CartShare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CartShareApplication {

    public static void main(String[] args) {
        System.out.println("Starting application");
        SpringApplication.run(CartShareApplication.class, args);
        System.out.println("Application started");
    }

}
