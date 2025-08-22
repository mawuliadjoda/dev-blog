
package com.esprit.notification.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdatedEvent {
    public String productId;
    public String sku;
    public String name;
    public int availableQuantity;
    public double price;
}
