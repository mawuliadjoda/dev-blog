package com.esprit.pagination.ports.input;

import com.esprit.pagination.domain.model.PaginatedData;
import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.domain.model.common.PageableQueryRequest;

public interface GetAllPaginatedProductsUseCase {
    PaginatedData<Product> getAllPaginatedProducts(PageableQueryRequest pageableQueryRequest);
}
