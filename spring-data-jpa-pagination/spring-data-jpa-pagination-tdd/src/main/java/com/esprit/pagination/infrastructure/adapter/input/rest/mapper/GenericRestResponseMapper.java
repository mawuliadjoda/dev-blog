package com.esprit.pagination.infrastructure.adapter.input.rest.mapper;

import com.esprit.pagination.domain.model.PaginatedData;

import java.util.List;

public interface GenericRestResponseMapper <D, R> {
    R toResponse(D d);
    List<R> toResponses(List<D> d);
    PaginatedData<R> toResponse(PaginatedData<D> dPaginatedData);
}
