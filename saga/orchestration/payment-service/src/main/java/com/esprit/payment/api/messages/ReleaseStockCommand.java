package com.esprit.payment.api.messages;

public record ReleaseStockCommand(String eventId, String orderId, String reason) {}
