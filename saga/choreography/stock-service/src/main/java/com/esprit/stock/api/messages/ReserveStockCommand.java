package com.esprit.stock.api.messages;

public record ReserveStockCommand(String eventId, String orderId, String sku, int qty) {}
