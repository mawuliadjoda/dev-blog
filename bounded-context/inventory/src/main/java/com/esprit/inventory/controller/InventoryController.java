package com.esprit.inventory.controller;

import com.esprit.inventory.controller.data.request.CreateProductRequest;
import com.esprit.inventory.persistence.entities.Product;
import com.esprit.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {


    private final InventoryService inventoryService;

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request) {
        Product product = inventoryService.createProduct(request);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = inventoryService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(inventoryService.getAll());
    }

    @GetMapping("/products/by-ids")
    public ResponseEntity<List<Product>> getProductsByIds(@RequestBody List<Long> productIds) {
        List<Product> products = inventoryService.findAllById(productIds);
        return ResponseEntity.ok(products);
    }

}
