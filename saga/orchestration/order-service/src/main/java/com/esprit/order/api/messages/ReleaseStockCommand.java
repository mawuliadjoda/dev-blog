package com.esprit.order.api.messages;

public record ReleaseStockCommand(String eventId, String orderId, String reason) {}
