package com.esprit.pagination.infrastructure.adapter.output.persistence.mapper;

import com.esprit.pagination.domain.model.common.PageableQueryRequest;
import com.esprit.pagination.domain.model.common.SortRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PageablePersistenceMapper {

    default PageRequest toPageable(PageableQueryRequest pageableQueryRequest) {
        return PageRequest.of(pageableQueryRequest.getPageNumber(), pageableQueryRequest.getPageSize(), toSort(pageableQueryRequest.getSort()));
    }

    default Sort toSort(SortRequest sortRequest) {
        if(sortRequest == null) return  null;
        return Sort.by(sortRequest.getOrderRequests().stream().map(orderRequest -> new Sort.Order(
                Sort.Direction.fromString(orderRequest.getDirection()), orderRequest.getProperty())).toList());
    }
}
