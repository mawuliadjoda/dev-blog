package com.esprit.spark.read;
import com.esprit.parquet.Person;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.List;

@Slf4j
public class HdfsParquetReader {

    public void read() {
        SparkSession spark = SparkSession.builder()
                .appName("Parquet HDFS Reader")
                .master("local[*]") // Exécuter Spark localement
                .config("spark.hadoop.fs.defaultFS", "hdfs://localhost:9000")
                .getOrCreate();


        //Dataset<Row> df = spark.read().parquet("hdfs://localhost:9000/data");
        Dataset<Row> df = spark.read().parquet("hdfs://localhost:9000/data/part-00000-dc46302e-c7e6-4bd2-af56-07e01a70a7f7-c000.snappy.parquet");


        df.printSchema();


        df.show();


        // Convertit en Dataset<Person>
        Dataset<Person> peoples = df.as(Encoders.bean(Person.class));


        peoples.show();

        List<Person> list = peoples.collectAsList();

        list.forEach(person -> System.out.println("##############Person #################  :"+ person.getName()));


        spark.stop();
    }
}
