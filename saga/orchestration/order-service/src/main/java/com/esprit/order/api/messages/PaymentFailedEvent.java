package com.esprit.order.api.messages;

public record PaymentFailedEvent(String eventId, String orderId, String reason) {}
