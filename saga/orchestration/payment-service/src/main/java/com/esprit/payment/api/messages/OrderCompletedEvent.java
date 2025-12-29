package com.esprit.payment.api.messages;

public record OrderCompletedEvent(String eventId, String orderId) {}
