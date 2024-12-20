package com.esprit.domain.search;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProductSearchCriteria {
    private String name;
    private String description;
}
