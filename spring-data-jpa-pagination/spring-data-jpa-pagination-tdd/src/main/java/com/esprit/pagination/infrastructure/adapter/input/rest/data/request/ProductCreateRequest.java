package com.esprit.pagination.infrastructure.adapter.input.rest.data.request;

import jakarta.validation.constraints.NotEmpty;

public record ProductCreateRequest(
        @NotEmpty(message = "name must not be empty")
        String name,
        @NotEmpty(message = "description must not be empty")
        String description) {
}