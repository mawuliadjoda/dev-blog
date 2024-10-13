package com.esprit.pagination.domain.service;

import com.esprit.pagination.domain.model.PaginatedData;
import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.domain.model.common.PageableQueryRequest;
import com.esprit.pagination.ports.input.CreateProductUseCase;
import com.esprit.pagination.ports.input.GetAllPaginatedProductsUseCase;
import com.esprit.pagination.ports.input.GetAllProductsUseCase;
import com.esprit.pagination.ports.output.ProductOutputPort;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ProductService implements CreateProductUseCase, GetAllProductsUseCase, GetAllPaginatedProductsUseCase {

    private final ProductOutputPort productOutputPort;


    @Override
    public Product createProduct(Product product) {
        return productOutputPort.createProduct(product);
    }

    @Override
    public List<Product> findAllProducts() {
        return productOutputPort.findAllProducts();
    }

    @Override
    public PaginatedData<Product> getAllPaginatedProducts(PageableQueryRequest pageableQueryRequest) {
        return productOutputPort.getAllPaginatedProducts(pageableQueryRequest);
    }
}
