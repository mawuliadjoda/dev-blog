package com.esprit.common.persistence.controller;

import com.esprit.common.persistence.entities.ProductEntity;
import com.esprit.common.persistence.projection.ProductDetail;
import com.esprit.common.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RepositoryRestController
public class ProductSearchController {

    private final ProductRepository productRepository;
    private final ProjectionFactory projectionFactory;



    @GetMapping("/products/search/custom")
    public ResponseEntity<?> searchWithProjection(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String projection
    ) {
        List<ProductEntity> entities = productRepository.findAll();

        // Ex : appliquer la projection "productDetail" si demandé
        if ("productDetail".equals(projection)) {
            List<Object> projected = Collections.singletonList(entities.stream()
                    .map(p -> projectionFactory.createProjection(ProductDetail.class, p))
                    .toList());
            return ResponseEntity.ok(projected);
        }

        // Default: return raw entities or another projection
        return ResponseEntity.ok(entities);
    }
}
