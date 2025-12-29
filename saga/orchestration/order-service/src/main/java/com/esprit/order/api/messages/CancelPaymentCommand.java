package com.esprit.order.api.messages;

public record CancelPaymentCommand(String eventId, String orderId, String reason) {}
