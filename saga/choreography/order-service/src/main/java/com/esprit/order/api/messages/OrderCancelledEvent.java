package com.esprit.order.api.messages;

public record OrderCancelledEvent(String eventId, String orderId, String reason) {}
