package com.esprit.pagination.infrastructure.adapter.input.rest;


import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.request.ProductCreateRequest;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.response.ProductCreateResponse;
import com.esprit.pagination.infrastructure.adapter.input.rest.mapper.ProductRestMapper;
import com.esprit.pagination.ports.input.CreateProductUseCase;
import com.esprit.pagination.ports.input.GetAllProductsUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductRestAdapter {

    private final CreateProductUseCase createProductUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;
    private final ProductRestMapper productRestMapper;

    @PostMapping
    ResponseEntity<ProductCreateResponse> createProduct(@RequestBody @Valid ProductCreateRequest productCreateRequest) {
        var product = productRestMapper.toProduct(productCreateRequest);
        product = createProductUseCase.createProduct(product);

        return new ResponseEntity<>(productRestMapper.toProductCreateResponse(product), HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<List<ProductCreateResponse>> findAllProducts() {
        List<Product> products = getAllProductsUseCase.findAllProducts();
        return new ResponseEntity<>(productRestMapper.toProductsCreateResponses(products), HttpStatus.OK);
    }
}
