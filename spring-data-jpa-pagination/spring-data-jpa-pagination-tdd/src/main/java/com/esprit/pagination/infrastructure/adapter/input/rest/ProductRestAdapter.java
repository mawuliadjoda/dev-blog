package com.esprit.pagination.infrastructure.adapter.input.rest;


import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.request.ProductCreateRequest;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.response.ProductQueryResponse;
import com.esprit.pagination.infrastructure.adapter.input.rest.mapper.ProductRestRequestMapper;
import com.esprit.pagination.infrastructure.adapter.input.rest.mapper.ProductRestResponseMapper;
import com.esprit.pagination.ports.input.CreateProductUseCase;
import com.esprit.pagination.ports.input.GetAllProductsUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductRestAdapter {

    private final CreateProductUseCase createProductUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;

    private final ProductRestRequestMapper productRestRequestMapper;
    private final ProductRestResponseMapper productRestResponseMapper;


    @PostMapping("/products")
    ResponseEntity<ProductQueryResponse> createProduct(@RequestBody @Valid ProductCreateRequest productCreateRequest) {
        var product = productRestRequestMapper.toDomain(productCreateRequest);
        product = createProductUseCase.createProduct(product);
        return new ResponseEntity<>(productRestResponseMapper.toResponse(product), HttpStatus.CREATED);
    }

    @GetMapping("/products")
    ResponseEntity<List<ProductQueryResponse>> findAllProducts() {
        List<Product> products = getAllProductsUseCase.findAllProducts();
        return new ResponseEntity<>(productRestResponseMapper.toResponses(products), HttpStatus.OK);
    }

}
