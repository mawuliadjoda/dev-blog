package com.studpay.template.adapters.inbound.rest.mapper;

import java.util.List;

public interface GenericRestResponseMapper <D, R> {
    R toResponse(D d);
    List<R> toResponses(List<D> d);
}