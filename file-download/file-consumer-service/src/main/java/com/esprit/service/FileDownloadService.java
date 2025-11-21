package com.esprit.service;


import com.esprit.config.FileStorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileDownloadService {

    private final RestClient restClient;

    private final FileStorageProperties props;

    public Path downloadAndSave(String fileName) throws IOException {
        try {

            byte[] fileBytes = restClient.get()
                    .uri("/api/files/{fileName}", fileName)
                    .retrieve()
                    .body(byte[].class);

            if (fileBytes == null) {
                throw new IOException("Downloaded file is empty or null");
            }

            Path dirPath = Paths.get(props.getDirectory());
            Files.createDirectories(dirPath);

            Path targetFile = dirPath.resolve(fileName);


            Files.write(targetFile, fileBytes);

            return targetFile;
        } catch (RestClientException e) {
            throw new IOException("Error while calling file-provider-service", e);
        }
    }
}
