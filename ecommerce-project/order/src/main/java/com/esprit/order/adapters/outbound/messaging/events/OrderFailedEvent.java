
package com.esprit.order.adapters.outbound.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderFailedEvent {
    private String orderId;
    private String reason;
}
