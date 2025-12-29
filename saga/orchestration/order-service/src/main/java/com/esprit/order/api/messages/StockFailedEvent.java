package com.esprit.order.api.messages;

public record StockFailedEvent(String eventId, String orderId, String reason) {}
