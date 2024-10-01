package com.esprit.pagination.infrastructure.adapter.input.rest;


import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.request.ProductCreateRequest;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.response.ProductCreateResponse;
import com.esprit.pagination.infrastructure.adapter.input.rest.mapper.ProductRestMapper;
import com.esprit.pagination.infrastructure.adapter.output.persistence.entity.ProductEntity;
import com.esprit.pagination.ports.input.CreateProductUseCase;
import com.esprit.pagination.ports.input.GetAllPaginatedProductsUseCase;
import com.esprit.pagination.ports.input.GetAllProductsUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductRestAdapter {

    private final CreateProductUseCase createProductUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;
    private final ProductRestMapper productRestMapper;


    @PostMapping("/products")
    ResponseEntity<ProductCreateResponse> createProduct(@RequestBody @Valid ProductCreateRequest productCreateRequest) {
        var product = productRestMapper.toProduct(productCreateRequest);
        product = createProductUseCase.createProduct(product);

        return new ResponseEntity<>(productRestMapper.toProductCreateResponse(product), HttpStatus.CREATED);
    }

    @GetMapping("/products")
    ResponseEntity<List<ProductCreateResponse>> findAllProducts() {
        List<Product> products = getAllProductsUseCase.findAllProducts();
        return new ResponseEntity<>(productRestMapper.toProductsCreateResponses(products), HttpStatus.OK);
    }

}
