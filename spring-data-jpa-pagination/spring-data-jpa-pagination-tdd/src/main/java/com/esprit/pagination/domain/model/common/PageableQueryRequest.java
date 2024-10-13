package com.esprit.pagination.domain.model.common;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageableQueryRequest {
    Integer pageNumber;
    Integer pageSize;
    SortRequest sort;
}
