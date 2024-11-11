package com.esprit.order.service;

import com.esprit.order.dto.CreateOrderRequest;
import com.esprit.order.dto.OrderItemResponse;
import com.esprit.order.dto.OrderResponse;
import com.esprit.order.dto.ProductDTO;
import com.esprit.order.exception.ResourceNotFoundException;
import com.esprit.order.peristence.entities.Customer;
import com.esprit.order.peristence.entities.Order;
import com.esprit.order.peristence.entities.OrderItem;
import com.esprit.order.peristence.repository.CustomerRepository;
import com.esprit.order.peristence.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    private final RestTemplate restTemplate; // Client pour appeler l'API REST du service d'inventaire

    private static final String INVENTORY_SERVICE_URL = "http://localhost:8083/api/inventory/products/";

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        // Créer une commande
        Order order = new Order();

        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(ResourceNotFoundException::new);

        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("CREATED");

        // Ajouter des articles de commande avec vérification de l'existence du produit
        List<OrderItem> items = request.getItems().stream().map(itemRequest -> {
            // Vérification de l'existence et récupération des informations du produit
            ProductDTO product = getProductById(itemRequest.getProductId());
            if (product == null) {
                throw new RuntimeException("Product with ID " + itemRequest.getProductId() + " does not exist");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(itemRequest.getProductId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(items);
        return orderRepository.save(order);
    }

    public OrderResponse getOrderWithProductDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> {
                    // Appel REST au service d'inventaire pour obtenir les détails du produit
                    ProductDTO product = getProductById(item.getProductId());
                    return new OrderItemResponse(
                            item.getProductId(),
                            product.getName(),
                            item.getQuantity(),
                            item.getPrice()
                    );
                })
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(), order.getOrderDate(), order.getStatus(), itemResponses);
    }

    // Méthode pour appeler l'API REST du service d'inventaire et obtenir un ProductDTO
    private ProductDTO getProductById(Long productId) {
        String url = INVENTORY_SERVICE_URL + productId;
        ResponseEntity<ProductDTO> response = restTemplate.getForEntity(url, ProductDTO.class);
        return response.getBody();
    }
}

