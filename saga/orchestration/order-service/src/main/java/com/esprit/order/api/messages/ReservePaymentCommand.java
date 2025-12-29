package com.esprit.order.api.messages;

import java.math.BigDecimal;

public record ReservePaymentCommand(String eventId, String orderId, BigDecimal amount) {}
