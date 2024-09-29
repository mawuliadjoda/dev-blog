package com.esprit.infrastructure.adapter.input.rest.mapper;


import com.esprit.infrastructure.adapter.input.rest.data.request.ProductCreateRequest;
import com.esprit.infrastructure.adapter.input.rest.data.response.ProductCreateResponse;
import com.esprit.infrastructure.adapter.input.rest.data.response.ProductQueryResponse;
import com.esprit.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductRestMapper {

    Product toProduct(ProductCreateRequest productCreateRequest);
    List<Product> toProducts(List<ProductCreateRequest> productCreateRequests);

    ProductCreateResponse toProductCreateResponse(Product product);
    List<ProductCreateResponse> toProductCreateResponses(List<Product> products);

    ProductQueryResponse toProductQueryResponse(Product product);
    List<ProductQueryResponse> toProductQueryResponses(List<Product> products);

}