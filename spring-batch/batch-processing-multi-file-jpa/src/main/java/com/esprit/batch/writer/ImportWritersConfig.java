package com.esprit.batch.writer;

import com.esprit.persistence.entities.StgCustomerEntity;
import com.esprit.persistence.entities.StgOrderEntity;
import com.esprit.persistence.repository.StgCustomerRepository;
import com.esprit.persistence.repository.StgOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ImportWritersConfig {

    private final StgCustomerRepository stgCustomerRepository;
    private final StgOrderRepository stgOrderRepository;

    @Bean
    public RepositoryItemWriter<StgCustomerEntity> stgCustomerWriter() {
        RepositoryItemWriter<StgCustomerEntity> writer = new RepositoryItemWriter<>();
        writer.setRepository(stgCustomerRepository);
        writer.setMethodName("save"); // ou saveAll, mais save marche très bien
        return writer;
    }

    @Bean
    public RepositoryItemWriter<StgOrderEntity> stgOrderWriter() {
        RepositoryItemWriter<StgOrderEntity> writer = new RepositoryItemWriter<>();
        writer.setRepository(stgOrderRepository);
        writer.setMethodName("save");
        return writer;
    }
}
