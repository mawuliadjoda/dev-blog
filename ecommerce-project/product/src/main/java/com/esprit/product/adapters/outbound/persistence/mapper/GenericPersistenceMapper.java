package com.esprit.product.adapters.outbound.persistence.mapper;

import java.util.List;

public interface GenericPersistenceMapper <E, D> {
    E toEntity(D d);
    D toDomain(E e);

    List<E> toEntities(List<D> dList);
    List<D> toDomains(List<E> eList);
}