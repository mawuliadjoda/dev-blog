
package com.esprit.product.adapters.inbound.rest;

import com.esprit.product.adapters.inbound.rest.data.request.ProductCreateRequest;
import com.esprit.product.adapters.inbound.rest.mapper.ProductRestRequestMapper;
import com.esprit.product.domain.model.Product;
import com.esprit.product.domain.service.ProductUseCases;
import com.esprit.security.config.jwt.CustomJwt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(value = "*")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductUseCases useCases;
    private final ProductRestRequestMapper  productRestRequestMapper;

    @GetMapping
    public List<Product> all() {
        return useCases.all();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@Valid @RequestBody ProductCreateRequest d) {
        displayToken();
        return useCases.create(productRestRequestMapper.toDomain(d));
        // return useCases.create(Product.builder().sku(d.sku).name(d.name).price(d.price).quantity(d.quantity).build());
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable String id, @Valid @RequestBody ProductCreateRequest d) {
        displayToken();
        return useCases.update(id, productRestRequestMapper.toDomain(d));
        // return useCases.update(id, Product.builder().sku(d.sku).name(d.name).price(d.price).quantity(d.quantity).build());
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable String id) {
        displayToken();
        return useCases.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        displayToken();
        useCases.delete(id);
    }

    @PatchMapping("/{id}/inventory")
    public Product inv(@PathVariable String id, @RequestParam int quantity) {
        displayToken();
        return useCases.updateInventory(id, quantity);
    }

    public void displayToken() {
        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        log.info("jwt token: {}", jwt.getToken().getTokenValue());
        log.info("jwt: {}", jwt);
        log.info("name: {}",jwt.getName());
    }
}
