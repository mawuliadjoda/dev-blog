package com.esprit.pagination.infrastructure.adapter.input.rest;

import com.esprit.pagination.domain.model.PaginatedResult;
import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.response.ProductCreateResponse;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.response.ProductQueryResponse;
import com.esprit.pagination.infrastructure.adapter.input.rest.mapper.PaginatedResultMapper;
import com.esprit.pagination.infrastructure.adapter.input.rest.mapper.ProductRestMapper;
import com.esprit.pagination.ports.input.GetAllPaginatedProductsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final ProductRestMapper productRestMapper;
    private final PaginatedResultMapper paginatedResultMapper;

    @GetMapping("/paginated-products")
    ResponseEntity<PaginatedResult<ProductQueryResponse>> getPaginatedProducts(Pageable pageable) {
        PaginatedResult<Product> productPage =  getAllPaginatedProductsUseCase.getAllPaginatedProducts(pageable);
        //Page<ProductQueryResponse> productCreateResponsePage = productPage.map(productRestMapper::toProductQueryResponse);
        return ResponseEntity.ok(paginatedResultMapper.toProductQueryResponsePage(productPage));
    }

}
