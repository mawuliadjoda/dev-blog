package com.esprit.payment.api.messages;

public record PaymentReservedEvent(String eventId, String orderId) {}
