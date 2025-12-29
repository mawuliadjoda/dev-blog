package com.esprit.stock.api.messages;

public record OrderCancelledEvent(String eventId, String orderId, String reason) {}
