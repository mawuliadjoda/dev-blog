package com.esprit.pagination.infrastructure.adapter.input.rest;

import com.esprit.pagination.domain.model.PaginatedData;
import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.domain.model.common.PageableQueryRequest;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.response.ProductQueryResponse;
import com.esprit.pagination.infrastructure.adapter.input.rest.mapper.ProductRestResponseMapper;
import com.esprit.pagination.infrastructure.adapter.input.rest.mapper.RestPageableRequestMapper;
import com.esprit.pagination.ports.input.GetAllPaginatedProductsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestAdapterPagination.class)
@AutoConfigureMockMvc
class ProductRestAdapterPaginationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    GetAllPaginatedProductsUseCase getAllPaginatedProductsUseCase;
    @MockBean
    ProductRestResponseMapper productRestResponseMapper;
    @MockBean
    RestPageableRequestMapper restPageableRequestMapper;

    @Test
    void getPaginatedProducts() throws Exception {
        // Given
        List<Product> products = List.of(
                new Product(1L, "Product 1", "Description 1"),
                new Product(2L, "Product 2", "Description 2")
        );
        List<ProductQueryResponse> productQueryResponses = List.of(
                new ProductQueryResponse(1L, "Product 1", "Description 1"),
                new ProductQueryResponse(2L, "Product 2", "Description 2")
        );
        PageableQueryRequest pageableQueryRequest = PageableQueryRequest.builder()
                .pageNumber(0)
                .pageSize(2)
                .build();

        Pageable pageable = PageRequest.of(0, 2);
        PaginatedData<Product> pagedProducts = new PaginatedData<>(products, 2L, products.size(), true);
        PaginatedData<ProductQueryResponse> pagedProductQueryResponses = new PaginatedData<>(productQueryResponses, 2L, productQueryResponses.size(), true);

        when(restPageableRequestMapper.toRequest(pageable)).thenReturn(pageableQueryRequest);

        when(getAllPaginatedProductsUseCase.getAllPaginatedProducts(pageableQueryRequest)).thenReturn(pagedProducts);
        when(productRestResponseMapper.toResponse(pagedProducts)).thenReturn(pagedProductQueryResponses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/paginated-products")
                        .param("page", "0")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Product 1"))
                .andExpect(jsonPath("$.content[1].name").value("Product 2"));
    }
}