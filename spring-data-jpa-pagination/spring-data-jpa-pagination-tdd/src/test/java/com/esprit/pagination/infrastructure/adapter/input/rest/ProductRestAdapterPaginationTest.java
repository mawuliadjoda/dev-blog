package com.esprit.pagination.infrastructure.adapter.input.rest;

import com.esprit.pagination.domain.model.PaginatedResult;
import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.response.ProductCreateResponse;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.response.ProductQueryResponse;
import com.esprit.pagination.infrastructure.adapter.input.rest.mapper.PaginatedResultMapper;
import com.esprit.pagination.infrastructure.adapter.input.rest.mapper.ProductRestMapper;
import com.esprit.pagination.ports.input.GetAllPaginatedProductsUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    ProductRestMapper productRestMapper;

    @MockBean
    PaginatedResultMapper paginatedResultMapper;

    @Test
    void getPaginatedProducts() throws Exception {
        // Given
        List<Product> products = List.of (
                new Product(1L, "Product 1", "Description 1"),
                new Product(2L, "Product 2", "Description 2")
        );
        List<ProductQueryResponse> productQueryResponses = List.of(
                new ProductQueryResponse(1L, "Product 1", "Description 1"),
                new ProductQueryResponse(2L, "Product 2", "Description 2")
        );
        Pageable pageable = PageRequest.of(0, 2);
        // Page<Product> pagedProducts = new PageImpl<>(products, pageable, products.size());
        PaginatedResult<Product> pagedProducts = new PaginatedResult<>(products, 0, products.size(), 2, 1);
        PaginatedResult<ProductQueryResponse> pagedProductQueryResponses = new PaginatedResult<>(productQueryResponses, 0, productQueryResponses.size(), 2, 1);
        when(getAllPaginatedProductsUseCase.getAllPaginatedProducts(pageable)).thenReturn(pagedProducts);
        //when(productRestMapper.toProductQueryResponses(products)).thenReturn(productQueryResponses);
        when(paginatedResultMapper.toProductQueryResponsePage(pagedProducts)).thenReturn(pagedProductQueryResponses);

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