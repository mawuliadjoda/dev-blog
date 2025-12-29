package com.esprit.stock.api.messages;

public record OrderCompletedEvent(String eventId, String orderId) {}
