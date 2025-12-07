package com.esprit.batch.reader;

import com.esprit.persistence.entities.common.BatchIdentifiable;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.function.Function;


public interface ImportCatalog {

    List<ImportDef<?>> catalog();

    record ImportDef<T extends BatchIdentifiable>(
            String name,
            String file,
            String[] columns,
            Function<FieldSet, T> mapper,
            JpaRepository<T, ?> repository
    ) {}
}
