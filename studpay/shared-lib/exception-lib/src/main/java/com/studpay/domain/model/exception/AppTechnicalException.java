package com.studpay.domain.model.exception;

public class AppTechnicalException extends RuntimeException {
    public AppTechnicalException(String message) {
        super(message);
    }
}
