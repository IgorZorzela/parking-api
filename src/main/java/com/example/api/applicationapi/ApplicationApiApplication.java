package com.example.api.applicationapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ApplicationApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(ApplicationApiApplication.class, args);
	}

	@GetMapping("/")
	public String index(){
		return "Hello World";
	}

}
