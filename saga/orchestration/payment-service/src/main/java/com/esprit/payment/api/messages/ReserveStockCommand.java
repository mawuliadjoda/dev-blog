package com.esprit.payment.api.messages;

public record ReserveStockCommand(String eventId, String orderId, String sku, int qty) {}
