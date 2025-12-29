package com.esprit.stock.api.messages;

public record ReleaseStockCommand(String eventId, String orderId, String reason) {}
