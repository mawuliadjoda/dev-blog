package com.esprit.batch.step;

import com.esprit.batch.processor.BatchIdEnricherProcessor;
import com.esprit.batch.reader.ImportCatalog;
import com.esprit.batch.reader.ImportReadersConfig;
import com.esprit.batch.writer.ImportWritersConfig;
import com.esprit.persistence.entities.common.BatchIdentifiable;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@RequiredArgsConstructor
public class ImportStagingStepsConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager tx;

    private final ImportReadersConfig importReadersConfig;
    private final ImportWritersConfig importWritersConfig;


    public <T extends BatchIdentifiable> Step importStep(ImportCatalog.ImportDef<T> def) {
        return new StepBuilder("import-" + def.name(), jobRepository)
                .<T, T>chunk(1000, tx)
                .reader(importReadersConfig.reader(def))
                .processor(new BatchIdEnricherProcessor<>())
                .writer(importWritersConfig.writer(def))
                .build();
    }

}

