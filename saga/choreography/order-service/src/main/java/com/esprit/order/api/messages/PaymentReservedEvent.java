package com.esprit.order.api.messages;

public record PaymentReservedEvent(String eventId, String orderId) {}
