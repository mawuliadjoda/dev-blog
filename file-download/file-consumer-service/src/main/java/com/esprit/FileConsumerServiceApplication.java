package com.esprit;

import com.esprit.config.FileProviderProperties;
import com.esprit.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ FileProviderProperties.class, FileStorageProperties.class })
public class FileConsumerServiceApplication {

	static void main(String[] args) {
		SpringApplication.run(FileConsumerServiceApplication.class, args);
	}

}
