package com.esprit.order.api.messages;

public record OrderCompletedEvent(String eventId, String orderId) {}
