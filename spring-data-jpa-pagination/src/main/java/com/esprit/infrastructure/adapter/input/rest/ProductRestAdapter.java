package com.esprit.infrastructure.adapter.input.rest;

import com.esprit.infrastructure.adapter.input.rest.data.request.ProductCreateRequest;
import com.esprit.infrastructure.adapter.input.rest.data.response.ProductCreateResponse;
import com.esprit.infrastructure.adapter.input.rest.data.response.ProductQueryResponse;
import com.esprit.infrastructure.adapter.input.rest.mapper.ProductRestMapper;
import com.esprit.application.ports.input.CreateProductUseCase;
import com.esprit.application.ports.input.GetProductUseCase;
import com.esprit.domain.model.Product;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductRestAdapter {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final ProductRestMapper productRestMapper;

    @PostMapping(value = "/products")
    public ResponseEntity<ProductCreateResponse> createProduct(@RequestBody @Valid ProductCreateRequest productCreateRequest) {
        // Rest to domain
        Product product = productRestMapper.toProduct(productCreateRequest);

        product = createProductUseCase.createProduct(product);

        // Domain to response
        return new ResponseEntity<>(productRestMapper.toProductCreateResponse(product), HttpStatus.CREATED);
    }

    @GetMapping(value = "/products/{id}")
    public ResponseEntity<ProductQueryResponse> getProduct(@PathVariable Long id) {
        Product product = getProductUseCase.getProductById(id);
        return new ResponseEntity<>(productRestMapper.toProductQueryResponse(product), HttpStatus.OK);
    }
}
