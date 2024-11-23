package com.esprit.principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.esprit"})
public class PrincipalApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrincipalApplication.class, args);
	}

}
