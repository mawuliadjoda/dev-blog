package com.esprit.payment.api.messages;

public record PaymentFailedEvent(String eventId, String orderId, String reason) {}
