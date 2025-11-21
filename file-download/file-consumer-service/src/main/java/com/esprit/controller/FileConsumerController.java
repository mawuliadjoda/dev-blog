package com.esprit.controller;


import com.esprit.service.FileDownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileConsumerController {

    private final FileDownloadService fileDownloadService;

    // http://localhost:8081/api/download/customers.csv
    @PostMapping("/download/{fileName}")
    public ResponseEntity<String> downloadFile(@PathVariable String fileName) {
        try {
            Path savedPath = fileDownloadService.downloadAndSave(fileName);
            return ResponseEntity.ok("Fichier téléchargé et sauvegardé sous : " + savedPath.toAbsolutePath());
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Erreur lors du téléchargement/sauvegarde du fichier : " + e.getMessage());
        }
    }
}
