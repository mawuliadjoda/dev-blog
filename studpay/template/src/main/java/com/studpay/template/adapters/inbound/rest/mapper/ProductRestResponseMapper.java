package com.studpay.template.adapters.inbound.rest.mapper;


import com.studpay.template.adapters.inbound.rest.data.response.ProductQueryResponse;
import com.studpay.template.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductRestResponseMapper extends GenericRestResponseMapper<Product, ProductQueryResponse> {
}
