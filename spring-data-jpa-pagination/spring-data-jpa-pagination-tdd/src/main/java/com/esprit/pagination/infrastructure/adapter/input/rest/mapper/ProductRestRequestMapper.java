package com.esprit.pagination.infrastructure.adapter.input.rest.mapper;

import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.request.ProductCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductRestRequestMapper extends GenericRestRequestMapper<ProductCreateRequest, Product> {
}
