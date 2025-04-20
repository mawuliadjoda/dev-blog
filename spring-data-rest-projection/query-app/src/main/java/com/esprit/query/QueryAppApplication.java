package com.esprit.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.esprit"})
@EntityScan("com.esprit.common.persistence.entities")
@EnableJpaRepositories("com.esprit.common.persistence.repository")
public class QueryAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueryAppApplication.class, args);
	}

}
