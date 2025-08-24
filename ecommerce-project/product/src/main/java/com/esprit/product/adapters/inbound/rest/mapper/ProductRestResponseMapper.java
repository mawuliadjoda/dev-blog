package com.esprit.product.adapters.inbound.rest.mapper;

import com.esprit.product.adapters.inbound.rest.data.response.ProductQueryResponse;
import com.esprit.product.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductRestResponseMapper extends GenericRestResponseMapper<Product, ProductQueryResponse> {
}
