package com.esprit.inventory.service;

import com.esprit.inventory.controller.data.request.CreateProductRequest;
import com.esprit.inventory.exception.ResourceNotFoundException;
import com.esprit.inventory.persistence.entities.Product;
import com.esprit.inventory.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final ProductRepository productRepository;

    public Product createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setDescription(request.getDescription());
        return productRepository.save(product);
    }
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product with ID " + productId + " not found"));
    }


    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<Product> findAllById(List<Long> productIds) {
        return productRepository.findAllById(productIds);  // Utilisation d'une méthode repository qui récupère plusieurs entités par leurs IDs
    }
}
