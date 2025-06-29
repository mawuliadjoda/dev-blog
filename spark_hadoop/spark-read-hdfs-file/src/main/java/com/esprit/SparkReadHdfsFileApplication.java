package com.esprit;

import com.esprit.spark.read.SparkHdfsParquetReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SparkReadHdfsFileApplication {

	public static void main(String[] args) {
		SpringApplication.run(SparkReadHdfsFileApplication.class, args);

		SparkHdfsParquetReader sparkHdfsParquetReader = new SparkHdfsParquetReader();
		sparkHdfsParquetReader.read();
	}

}
