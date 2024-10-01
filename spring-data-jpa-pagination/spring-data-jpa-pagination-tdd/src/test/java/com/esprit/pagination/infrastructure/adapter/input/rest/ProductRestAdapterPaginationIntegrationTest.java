package com.esprit.pagination.infrastructure.adapter.input.rest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/*
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/test-data.sql")

 */
public class ProductRestAdapterPaginationIntegrationTest {

    /*
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnPagedProducts() throws Exception {
        mockMvc.perform(get("/api/v1/paginated-products")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2)) // Vérifie qu'il y a 2 produits sur la page 0
                .andExpect(jsonPath("$.content[0].name").value("Product 1"))
                .andExpect(jsonPath("$.content[1].name").value("Product 2"));
    }

    @Test
    void shouldReturnSecondPage() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .param("page", "1")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2)) // Vérifie qu'il y a 2 produits sur la page 1
                .andExpect(jsonPath("$.content[0].name").value("Product 3"))
                .andExpect(jsonPath("$.content[1].name").value("Product 4"));
    }

     */

}
