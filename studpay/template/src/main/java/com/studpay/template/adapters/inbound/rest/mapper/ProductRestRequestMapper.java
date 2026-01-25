package com.esprit.product.adapters.inbound.rest.mapper;

import com.esprit.product.adapters.inbound.rest.data.request.ProductCreateRequest;
import com.esprit.product.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductRestRequestMapper extends GenericRestRequestMapper<ProductCreateRequest, Product> {
}