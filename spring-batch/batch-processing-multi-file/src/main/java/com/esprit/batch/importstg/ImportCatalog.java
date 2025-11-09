package com.esprit.batch.importstg;

import org.springframework.batch.item.file.transform.FieldSet;

import java.util.List;
import java.util.function.Function;

public interface ImportCatalog {
    List<ImportDef<?>> catalog();

    record ImportDef<T>(String name, String file, String[] columns, Function<FieldSet, T> mapper, String upsertSql) {}
}
