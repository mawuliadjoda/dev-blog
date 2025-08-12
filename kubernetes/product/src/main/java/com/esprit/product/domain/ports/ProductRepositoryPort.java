
package com.esprit.product.domain.ports;


import com.esprit.product.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
    Product save(Product p);

    Optional<Product> findById(String id);

    Optional<Product> findBySku(String sku);

    List<Product> findAll();
}
