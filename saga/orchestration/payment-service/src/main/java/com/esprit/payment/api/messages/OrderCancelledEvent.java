package com.esprit.payment.api.messages;

public record OrderCancelledEvent(String eventId, String orderId, String reason) {}
