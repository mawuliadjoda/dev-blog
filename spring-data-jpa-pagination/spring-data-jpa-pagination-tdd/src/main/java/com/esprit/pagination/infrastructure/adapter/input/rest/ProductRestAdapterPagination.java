package com.esprit.pagination.infrastructure.adapter.input.rest;

import com.esprit.pagination.domain.model.PaginatedData;
import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.domain.model.common.PageableQueryRequest;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.response.ProductQueryResponse;
import com.esprit.pagination.infrastructure.adapter.input.rest.mapper.ProductRestResponseMapper;
import com.esprit.pagination.infrastructure.adapter.input.rest.mapper.RestPageableRequestMapper;
import com.esprit.pagination.ports.input.GetAllPaginatedProductsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductRestAdapterPagination {
    private final GetAllPaginatedProductsUseCase getAllPaginatedProductsUseCase;
    private final ProductRestResponseMapper productRestResponseMapper;
    private final RestPageableRequestMapper restPageableRequestMapper;

    @GetMapping("/paginated-products")
    ResponseEntity<PaginatedData<ProductQueryResponse>> getPaginatedProducts(Pageable pageable) {

        PageableQueryRequest pageableQueryRequest = restPageableRequestMapper.toRequest(pageable);

        PaginatedData<Product> productPage = getAllPaginatedProductsUseCase.getAllPaginatedProducts(pageableQueryRequest);
        PaginatedData<ProductQueryResponse> response = productRestResponseMapper.toResponse(productPage);
        return ResponseEntity.ok(response);
    }

}
