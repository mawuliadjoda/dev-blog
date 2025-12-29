package com.esprit.stock.api.messages;

public record StockFailedEvent(String eventId, String orderId, String reason) {}
