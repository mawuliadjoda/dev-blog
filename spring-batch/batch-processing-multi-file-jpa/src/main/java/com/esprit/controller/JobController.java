package com.esprit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/job")
@RequiredArgsConstructor
@Slf4j
public class JobController {

    private final JobLauncher jobLauncher;


    private final Job importToStagingJob;


    @PostMapping("/run-import")
    public ResponseEntity<String> runImport() throws Exception {
        String batchId = UUID.randomUUID().toString();
        JobParameters params = new JobParametersBuilder()
                .addString("batchId", batchId)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(importToStagingJob, params);
        return ResponseEntity.ok("Import lancé, batchId = " + batchId);
    }
}
