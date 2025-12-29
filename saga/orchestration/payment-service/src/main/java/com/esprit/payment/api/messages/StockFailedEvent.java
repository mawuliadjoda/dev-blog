package com.esprit.payment.api.messages;

public record StockFailedEvent(String eventId, String orderId, String reason) {}
