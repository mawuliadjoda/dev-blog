package com.esprit.stock.api.messages;

public record CancelPaymentCommand(String eventId, String orderId, String reason) {}
