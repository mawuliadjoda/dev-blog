package com.esprit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
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
    // private final Job importStagingJob;
    // private final Job importDynamicStagingJob;
    // private final Job importToStaging;
    //private final Job importToStagingParalelStep;



    /*
    @PostMapping(value = "/import-csv-to-customers")
    public void importCsvToCustomers() {
        String batchId = UUID.randomUUID().toString();
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("batchId", batchId)
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(importToStagingParalelStep, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            log.error(e.getMessage());
        }
    }

     */

    private final Job importToStagingJob;   // Job 1 (import seul)
    private final Job importThenCleanJob;   // Job 2 (import → clean)

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

    @PostMapping("/run-import-clean")
    public ResponseEntity<String> runImportThenClean() throws Exception {
        String batchId = UUID.randomUUID().toString();
        JobParameters params = new JobParametersBuilder()
                .addString("batchId", batchId)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(importThenCleanJob, params);
        return ResponseEntity.ok("Import + Clean lancés, batchId = " + batchId);
    }


}
