package com.esprit.order.service;

import com.esprit.order.dto.OrderItemResponse;
import com.esprit.order.dto.OrderResponse;
import com.esprit.order.dto.ProductDTO;
import com.esprit.order.peristence.entities.Order;
import com.esprit.order.peristence.entities.OrderItem;
import com.esprit.order.peristence.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceOptimization {

    private final OrderRepository orderRepository;

    private final RestTemplate restTemplate; // Client pour appeler l'API REST du service d'inventaire

    private static final String INVENTORY_SERVICE_URL = "http://inventory-service/api/inventory/products/";

    public OrderResponse getOrderWithProductDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Extraire tous les productIds uniques de la commande
        List<Long> productIds = order.getItems().stream()
                .map(OrderItem::getProductId)
                .distinct()
                .collect(Collectors.toList());

        // Appel REST pour récupérer les informations des produits par lots
        List<ProductDTO> products = getProductsByIds(productIds);

        // Créer un map pour un accès rapide aux produits par leur ID
        Map<Long, ProductDTO> productMap = products.stream()
                .collect(Collectors.toMap(ProductDTO::getId, product -> product));

        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> {
                    ProductDTO product = productMap.get(item.getProductId());
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

    private List<ProductDTO> getProductsByIds(List<Long> productIds) {
        String url = INVENTORY_SERVICE_URL + "products"; // Exemple d'URL du service d'inventaire
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Créez un objet HttpEntity pour envoyer la liste d'IDs dans le corps de la requête
        HttpEntity<List<Long>> entity = new HttpEntity<>(productIds, headers);

        // Utilisation de exchange avec méthode POST et envoi dans le corps
        ResponseEntity<List<ProductDTO>> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
                }
        );

        return response.getBody();
    }

}

