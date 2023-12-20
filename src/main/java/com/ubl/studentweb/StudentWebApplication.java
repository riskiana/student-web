package com.ubl.studentweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {
SecurityAutoConfiguration.class })
@EnableJpaRepositories("com.ubl.studentweb.repository")
@ComponentScan({
	"com.ubl.studentweb.repository",
	"com.ubl.studentweb.service",
	"com.ubl.studentweb.rest",
	"com.ubl.studentweb.domain",
})
public class StudentWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentWebApplication.class, args);
	}


}
