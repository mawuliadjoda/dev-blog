package com.esprit.batch.downloadfile;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Path;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DownloadConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager tx;
    private final FileDownloadService fileDownloadService;

    private final FileStorageProperties fileStorageProperties;

    private final String customerFileName = "customers.csv";
    private final String orderFileName = "orders.csv";

    @Bean
    public Step downloadStep() {
        return new StepBuilder("downloadStep", jobRepository)
                .tasklet((contrib, ctx) -> {
                    Path curstomerPath = fileDownloadService.downloadAndSave(customerFileName);
                    Path orderPath = fileDownloadService.downloadAndSave(orderFileName);

                    log.info("Fichier téléchargé et sauvegardé sous : {}{}", curstomerPath.toAbsolutePath(), orderPath.toAbsolutePath());
                    return RepeatStatus.FINISHED;
                }, tx)
                .build();
    }

    @Bean
    public Step deleteDownloadStep() {
        return new StepBuilder("deleteDownloadStep", jobRepository)
                .tasklet((contrib, ctx) -> {

                    boolean result = FileSystemUtils.deleteRecursively(Path.of(fileStorageProperties.getDirectory()));

                    log.info("Downloaded folder deleted : {}", result);
                    return RepeatStatus.FINISHED;
                }, tx)
                .build();
    }
}
