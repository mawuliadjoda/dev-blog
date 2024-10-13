package com.esprit.pagination.infrastructure.adapter.input.rest.mapper;

import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.response.ProductQueryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductRestResponseMapper extends GenericRestResponseMapper<Product, ProductQueryResponse> {
}
