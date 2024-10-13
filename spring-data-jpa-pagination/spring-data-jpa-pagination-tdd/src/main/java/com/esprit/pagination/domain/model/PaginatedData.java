package com.esprit.pagination.domain.model;

import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedData<T> {
    private List<T> content;
    private Long totalElements;
    private Integer totalPages;
    private Boolean last;
}
