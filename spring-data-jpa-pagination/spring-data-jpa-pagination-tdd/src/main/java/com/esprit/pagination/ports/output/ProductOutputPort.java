package com.esprit.pagination.ports.output;

import com.esprit.pagination.domain.model.PaginatedData;
import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.domain.model.common.PageableQueryRequest;

import java.util.List;

public interface ProductOutputPort {
    Product createProduct(Product product);

    List<Product> findAllProducts();

    PaginatedData<Product> getAllPaginatedProducts(PageableQueryRequest pageableQueryRequest);
}
