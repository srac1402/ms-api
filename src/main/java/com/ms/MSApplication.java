package com.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.ms" })
public class MSApplication {
	public static void main(String[] args) {
		SpringApplication.run(MSApplication.class, args);
	}
}
