package com.esprit.batch.importstg;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "batch.import")
@Data
public class ImportProperties {
    private String customersFile; // ex: classpath:customers.csv
    private String ordersFile;    // ex: classpath:orders.csv
    // listes autorisées (utiles aussi pour DQ si tu veux centraliser)
    private List<String> allowedStatuses = List.of("NEW","PAID","CANCELLED","SHIPPED","REFUNDED");
    private List<String> allowedGenders  = List.of("Male","Female","Other","Unknown");
}