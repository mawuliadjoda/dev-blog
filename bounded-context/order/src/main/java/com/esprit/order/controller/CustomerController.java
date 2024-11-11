package com.esprit.order.controller;

import com.esprit.order.controller.data.request.CreatCustomerRequest;
import com.esprit.order.peristence.entities.Customer;
import com.esprit.order.peristence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerRepository customerRepository;

    @PostMapping("/customers")
    public ResponseEntity<Customer> createProduct(@RequestBody CreatCustomerRequest request) {
        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
        Customer saveCustomer = customerRepository.save(customer);
        return ResponseEntity.ok(saveCustomer);
    }

    @GetMapping("/customers/{productId}")
    public ResponseEntity<Customer> getProductById(@PathVariable Long productId) {
        Customer product = customerRepository.getReferenceById(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(customerRepository.findAll());
    }
}
