package com.esprit.batch.reader;

import com.esprit.persistence.entities.common.BatchIdentifiable;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImportReadersConfig {

    private final ResourceLoader resourceLoader;

    public <T extends BatchIdentifiable> FlatFileItemReader<T> reader(ImportCatalog.ImportDef<T> def) {
        var reader = new FlatFileItemReader<T>();
        reader.setName("reader-" + def.name());
        reader.setResource(resourceLoader.getResource(def.file()));
        reader.setLinesToSkip(1);

        var lineMapper = new DefaultLineMapper<T>();
        var tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setStrict(false);
        tokenizer.setNames(def.columns());
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(def.mapper()::apply);

        reader.setLineMapper(lineMapper);
        return reader;
    }
}
