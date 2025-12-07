package com.esprit;

import com.esprit.properties.ImportProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ ImportProperties.class })
public class BatchProcessingMultiFileJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchProcessingMultiFileJpaApplication.class, args);
	}

}
