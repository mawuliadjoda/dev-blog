package com.esprit.principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.esprit"})
@EntityScan(basePackages = "com.esprit")
@EnableJpaRepositories(basePackages = "com.esprit")
public class PrincipalApplication {

	public static void main(String[] args) {
		System.setProperty("liquibase.duplicateFileMode", "WARN"); // <-- avant
		SpringApplication.run(PrincipalApplication.class, args);
	}

}
