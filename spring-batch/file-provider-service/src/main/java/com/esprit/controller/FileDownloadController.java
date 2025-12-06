package com.esprit.controller;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileDownloadController {

    // http://localhost:8080/api/files/orders.csv
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadMyFile(@PathVariable String fileName) throws IOException {

        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            return ResponseEntity.badRequest().build();
        }

        // On suppose que les fichiers sont dans src/main/resources/data
        Resource file = new ClassPathResource("data/" + fileName);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentLength(file.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}
