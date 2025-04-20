package com.esprit.query.controller;

import com.esprit.common.persistence.entities.ProductEntity;
import com.esprit.common.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ProductCommandController {

    private final ProductRepository repository;


    @GetMapping("/products")
    public ResponseEntity<List<ProductEntity>> createProduct() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping("/products")
    public ResponseEntity<ProductEntity> createProduct(@RequestBody ProductEntity product) {
        return ResponseEntity.ok(repository.save(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductEntity> update(@PathVariable Long id, @RequestBody ProductEntity updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setPrice(updated.getPrice());
                    return ResponseEntity.ok(repository.save(existing));
                }).orElse(ResponseEntity.notFound().build());
    }
}
