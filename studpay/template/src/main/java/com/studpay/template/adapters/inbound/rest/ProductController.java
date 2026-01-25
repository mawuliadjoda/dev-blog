
package com.studpay.template.adapters.inbound.rest;


import com.studpay.template.adapters.inbound.rest.data.request.ProductCreateRequest;
import com.studpay.template.adapters.inbound.rest.mapper.ProductRestRequestMapper;
import com.studpay.template.domain.model.Product;
import com.studpay.template.domain.service.ProductUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(value = "*")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductUseCases useCases;
    private final ProductRestRequestMapper productRestRequestMapper;

    @GetMapping
    public List<Product> all() {
        return useCases.all();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@Valid @RequestBody ProductCreateRequest d) {
        return useCases.create(productRestRequestMapper.toDomain(d));
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable String id, @Valid @RequestBody ProductCreateRequest d) {
        return useCases.update(id, productRestRequestMapper.toDomain(d));
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable String id) {
        return useCases.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        useCases.delete(id);
    }

    @PatchMapping("/{id}/inventory")
    public Product inv(@PathVariable String id, @RequestParam int quantity) {
        return useCases.updateInventory(id, quantity);
    }
}
