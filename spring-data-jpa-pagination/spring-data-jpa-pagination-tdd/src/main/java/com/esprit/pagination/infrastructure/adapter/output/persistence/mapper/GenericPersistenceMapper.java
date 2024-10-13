package com.esprit.pagination.infrastructure.adapter.output.persistence.mapper;

import com.esprit.pagination.domain.model.PaginatedData;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GenericPersistenceMapper <E, D> {
    E toEntity(D d);
    D toDomain(E e);

    List<E> toEntities(List<D> dList);
    List<D> toDomains(List<E> eList);

    PaginatedData<D> toDomain(Page<E> ePage);
}
