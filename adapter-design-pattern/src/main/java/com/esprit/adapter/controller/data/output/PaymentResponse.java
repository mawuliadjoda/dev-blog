package com.esprit.adapter.controller.data.output;

public record PaymentResponse(boolean isSucces, Long transactionId) {
    public PaymentResponse {
        if( transactionId < 0 ) {
            throw new IllegalArgumentException("Transaction Id cannot be negative");
        }
    }
}
