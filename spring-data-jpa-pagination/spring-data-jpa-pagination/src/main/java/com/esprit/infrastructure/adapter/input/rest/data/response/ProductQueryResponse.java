package com.esprit.infrastructure.adapter.input.rest.data.response;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductQueryResponse {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private String priceOperation;
}