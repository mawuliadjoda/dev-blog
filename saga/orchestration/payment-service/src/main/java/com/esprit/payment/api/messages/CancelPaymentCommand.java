package com.esprit.payment.api.messages;

public record CancelPaymentCommand(String eventId, String orderId, String reason) {}
