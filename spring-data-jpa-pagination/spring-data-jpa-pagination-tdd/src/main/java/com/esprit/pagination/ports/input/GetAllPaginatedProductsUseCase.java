package com.esprit.pagination.ports.input;

import com.esprit.pagination.domain.model.PaginatedResult;
import com.esprit.pagination.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetAllPaginatedProductsUseCase {
    PaginatedResult<Product> getAllPaginatedProducts(Pageable pageable);
}
