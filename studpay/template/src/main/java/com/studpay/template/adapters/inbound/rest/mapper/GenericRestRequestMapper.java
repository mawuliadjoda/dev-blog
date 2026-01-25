package com.studpay.template.adapters.inbound.rest.mapper;

public interface GenericRestRequestMapper<R, D> {
    D toDomain(R request);
}