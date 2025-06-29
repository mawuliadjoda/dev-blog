package com.esprit.parquet;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Encoders;

import java.util.Arrays;
import java.util.List;

public class ParquetFileWriter {
    public void generate() {
        SparkSession spark = SparkSession.builder()
                .appName("WriteSampleParquet")
                .master("local[*]")
                .getOrCreate();

        List<Person> people = Arrays.asList(
                new Person(1, "Alice", "Engineer"),
                new Person(2, "Bob", "Manager"),
                new Person(3, "Charlie", "Analyst")
        );

        Dataset<Person> df = spark.createDataset(people, Encoders.bean(Person.class));
        df.coalesce(1).write().parquet("/mnt/d/dev/git/dev-blog/spark_hadoop/generate-parquet-file/data/person2.parquet");

        spark.stop();

    }

    public void csvToParquet() {
        SparkSession spark = SparkSession.builder()
                .appName("WriteSampleParquet")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> df = spark.read()
                .option("header", "true")
                .csv("/mnt/d/dev/git/dev-blog/spark_hadoop/generate-parquet-file/data/data.csv");

        df.write().parquet("/mnt/d/dev/git/dev-blog/spark_hadoop/generate-parquet-file/data/sample.parquet");

    }
}
