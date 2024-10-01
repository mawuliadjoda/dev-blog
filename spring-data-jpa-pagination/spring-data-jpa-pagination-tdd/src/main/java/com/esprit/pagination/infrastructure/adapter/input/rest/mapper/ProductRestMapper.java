package com.esprit.pagination.infrastructure.adapter.input.rest.mapper;

import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.request.ProductCreateRequest;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.response.ProductCreateResponse;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.response.ProductQueryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductRestMapper {

    Product toProduct(ProductCreateRequest productCreateRequest);

    ProductCreateResponse toProductCreateResponse(Product product);

    List<ProductCreateResponse> toProductsCreateResponses(List<Product> products);

    Product toProduct(ProductQueryResponse productQueryResponse);
    List<Product> toProducts(List<ProductQueryResponse> productQueryResponses);

    ProductQueryResponse toProductQueryResponse(Product product);
    List<ProductQueryResponse> toProductQueryResponses(List<Product> products);

}
