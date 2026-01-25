
package com.studpay.template.domain.service;




import com.studpay.domain.model.exception.AppNotFoundException;
import com.studpay.template.domain.model.Product;
import com.studpay.template.domain.ports.ProductEventPublisher;
import com.studpay.template.domain.ports.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductUseCases {
    private final ProductRepositoryPort repo;
    private final ProductEventPublisher publisher;

    public Product create(Product p) {
        var s = repo.save(p);
        publisher.publishProductUpdated(s);
        return s;
    }

    public Product update(String id, Product patch) {
        var e = repo.findById(id).orElseThrow(() -> new AppNotFoundException("Product not found"));
        e.setName(patch.getName());
        e.setPrice(patch.getPrice());
        e.setQuantity(patch.getQuantity());
        var s = repo.save(e);
        publisher.publishProductUpdated(s);
        return s;
    }

    public Product findById(String id) {
        return repo.findById(id).orElseThrow(() -> new AppNotFoundException("Product not found"));
    }

    public Product updateInventory(String id, int q) {
        var e = repo.findById(id).orElseThrow(() -> new AppNotFoundException("Product not found"));
        e.setQuantity(q);
        var s = repo.save(e);
        publisher.publishProductUpdated(s);
        return s;
    }

    public List<Product> all() {
        return repo.findAll();
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}
