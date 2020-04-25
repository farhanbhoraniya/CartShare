package com.cmpe275.CartShare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CartShareApplication {

	public static void main(String[] args) {
		System.out.println("Starting application");
		SpringApplication.run(CartShareApplication.class, args);
		System.out.println("Application started");
	}

}
