package com.esprit;


import com.esprit.spark.read.HdfsParquetReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GenerateParquetFileApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenerateParquetFileApplication.class, args);

		// ParquetFileWriter parquetFileWriter = new ParquetFileWriter();
		// parquetFileWriter.generate();

		HdfsParquetReader hdfsParquetReader = new HdfsParquetReader();
		hdfsParquetReader.read();
	}

}
