package com.esprit.pagination.infrastructure.adapter.input.rest.mapper;

import com.esprit.pagination.domain.model.PaginatedResult;
import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.response.ProductCreateResponse;
import com.esprit.pagination.infrastructure.adapter.input.rest.data.response.ProductQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaginatedResultMapper {

    private final ProductRestMapper productRestMapper;
    public <T, U> PaginatedResult<U> mapPaginatedResult(PaginatedResult<T> source, List<U> content) {
        return new PaginatedResult<>(
                content,
                source.getPageNumber(),
                source.getPageSize(),
                source.getTotalElements(),
                source.getTotalPages()
        );
    }

    public PaginatedResult<ProductQueryResponse> toProductQueryResponsePage(PaginatedResult<Product> productPage) {
        List<ProductQueryResponse> mappedContent = productPage.getContent().stream()
                .map(productRestMapper::toProductQueryResponse)
                .collect(Collectors.toList());

        return mapPaginatedResult(productPage, mappedContent);
    }
}
