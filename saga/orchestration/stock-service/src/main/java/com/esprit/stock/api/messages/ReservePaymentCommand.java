package com.esprit.stock.api.messages;

import java.math.BigDecimal;

public record ReservePaymentCommand(String eventId, String orderId, BigDecimal amount) {}
