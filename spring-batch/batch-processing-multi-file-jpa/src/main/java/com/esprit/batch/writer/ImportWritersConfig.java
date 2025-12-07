package com.esprit.batch.writer;

import com.esprit.batch.reader.ImportCatalog;
import com.esprit.persistence.entities.common.BatchIdentifiable;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImportWritersConfig {

    public <T extends BatchIdentifiable> RepositoryItemWriter<T> writer(ImportCatalog.ImportDef<T> def) {
        var w = new RepositoryItemWriter<T>();
        w.setRepository(def.repository());
        w.setMethodName("save");
        return w;
    }

}
