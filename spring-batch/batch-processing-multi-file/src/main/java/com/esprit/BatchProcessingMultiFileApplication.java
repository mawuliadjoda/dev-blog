package com.esprit;

import com.esprit.batch.downloadfile.FileProviderProperties;
import com.esprit.batch.downloadfile.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class, FileProviderProperties.class })
public class BatchProcessingMultiFileApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchProcessingMultiFileApplication.class, args);
	}

}
