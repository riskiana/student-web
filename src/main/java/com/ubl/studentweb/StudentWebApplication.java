package com.ubl.studentweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ubl.studentweb")
public class StudentWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(StudentWebApplication.class, args);
	}

}
