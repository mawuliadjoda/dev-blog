package com.esprit.pagination.infrastructure.adapter.input.rest.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductQueryResponse {
    private Long id;
    private String name;
    private String description;
}
