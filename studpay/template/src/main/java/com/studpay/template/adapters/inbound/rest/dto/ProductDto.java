
package com.studpay.template.adapters.inbound.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    public String id;
    @NotBlank
    public String name;
    @PositiveOrZero
    public double price;
    @PositiveOrZero
    public int quantity;
}
