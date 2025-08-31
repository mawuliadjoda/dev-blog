
package com.esprit.product.domain.ports;


import com.esprit.product.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
    Product save(Product p);

    Optional<Product> findById(String id);

    Optional<Product> findByName(String name);

    List<Product> findAll();

    void deleteById(String id);
}
