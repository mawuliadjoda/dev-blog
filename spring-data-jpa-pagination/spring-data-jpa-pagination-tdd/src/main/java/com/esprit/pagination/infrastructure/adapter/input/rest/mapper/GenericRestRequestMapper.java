package com.esprit.pagination.infrastructure.adapter.input.rest.mapper;

public interface GenericRestRequestMapper<R, D> {
    D toDomain(R request);
}
