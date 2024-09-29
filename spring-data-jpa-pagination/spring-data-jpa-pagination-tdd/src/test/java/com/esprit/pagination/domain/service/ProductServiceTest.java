package com.esprit.pagination.domain.service;

import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.ports.output.ProductOutputPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductOutputPort productOutputPort;

    Product product;


    @Test
    void createProduct() {
        Product product = Product.builder()
                .name("test")
                .description("test")
                .build();

        Product createdProduct = Product.builder()
                .id(1L)
                .name("test")
                .description("test")
                .build();
        // Given
        given(productOutputPort.createProduct(product)).willReturn(createdProduct);

        // When
        Product created = productService.createProduct(product);

        // Then
        verify(productOutputPort, Mockito.times(1)).createProduct(product);
        Assertions.assertThat(createdProduct).usingRecursiveComparison().isEqualTo(created);


    }

    @Test
    void findAllProducts() {
        // Given
        Product product1 = Product.builder()
                .id(1L)
                .name("test")
                .description("test")
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("test2")
                .description("test2")
                .build();
        List<Product> products = List.of(product1, product2);
        given(productOutputPort.findAllProducts()).willReturn(products);

        // When
        List<Product> allProducts = productService.findAllProducts();
        Assertions.assertThat(products).usingRecursiveFieldByFieldElementComparator().containsExactlyElementsOf(allProducts);

    }
}