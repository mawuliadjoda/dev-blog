package com.esprit.pagination.infrastructure.adapter.input.rest.mapper;

import com.esprit.pagination.domain.model.common.OrderRequest;
import com.esprit.pagination.domain.model.common.PageableQueryRequest;
import com.esprit.pagination.domain.model.common.SortRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RestPageableRequestMapper {
    PageableQueryRequest toRequest(Pageable pageable);

    default SortRequest toSortRequest(Sort sort) {
        return new SortRequest(sort.stream().map(order -> new OrderRequest(order.getDirection().name(), order.getProperty())).toList());
    }
}
