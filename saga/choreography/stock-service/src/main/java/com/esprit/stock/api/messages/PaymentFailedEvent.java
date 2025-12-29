package com.esprit.stock.api.messages;

public record PaymentFailedEvent(String eventId, String orderId, String reason) {}
