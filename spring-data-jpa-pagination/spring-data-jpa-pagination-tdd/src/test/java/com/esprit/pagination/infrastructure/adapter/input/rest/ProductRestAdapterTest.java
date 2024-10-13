package com.esprit.pagination.infrastructure.adapter.input.rest;


import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.request.ProductCreateRequest;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.response.ProductQueryResponse;
import com.esprit.pagination.infrastructure.adapter.input.rest.mapper.ProductRestRequestMapper;
import com.esprit.pagination.infrastructure.adapter.input.rest.mapper.ProductRestResponseMapper;
import com.esprit.pagination.ports.input.CreateProductUseCase;
import com.esprit.pagination.ports.input.GetAllProductsUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductRestAdapter.class)
@AutoConfigureMockMvc
public class ProductRestAdapterTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CreateProductUseCase createProductUseCase;

    @MockBean
    GetAllProductsUseCase getAllProductsUseCase;

    @MockBean
    ProductRestRequestMapper productRestRequestMapper;
    @MockBean
    ProductRestResponseMapper productRestResponseMapper;

    @Test
    void shouldCreateProduct() throws Exception {
        // Given
        var productCreateRequest = new ProductCreateRequest("test", "test");
        var productQueryResponse = new ProductQueryResponse(1L, "test", "test");

        Product productRequest = Product.builder()
                .name("test")
                .description("test")
                .build();
        Product productResponse = Product.builder()
                .id(1L)
                .name("test")
                .description("test")
                .build();

        given(productRestRequestMapper.toDomain(productCreateRequest)).willReturn(productRequest);
        given(createProductUseCase.createProduct(productRequest)).willReturn(productResponse);
        given(productRestResponseMapper.toResponse(productResponse)).willReturn(productQueryResponse);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(productQueryResponse)));
    }

    @Test
    void souhldReturnAllProducts() throws Exception {
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

        var productQueryResponse1 = new ProductQueryResponse(1L, "test", "test");
        var productQueryResponse2 = new ProductQueryResponse(2L, "test2", "test2");
        List<ProductQueryResponse> productCreateResponses = List.of(productQueryResponse1, productQueryResponse2);

        given(getAllProductsUseCase.findAllProducts()).willReturn(products);
        given(productRestResponseMapper.toResponses(products)).willReturn(productCreateResponses);


        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productCreateResponses)));
    }
}
