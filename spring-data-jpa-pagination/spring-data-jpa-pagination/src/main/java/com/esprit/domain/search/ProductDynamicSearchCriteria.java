package com.esprit.domain.search;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProductDynamicSearchCriteria {
    private List<String> names;
    private List<String> descriptions;
    private Double price;
    private String priceOperation;
}
