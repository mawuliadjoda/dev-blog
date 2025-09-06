package com.esprit.domain.model.exception;


import lombok.Getter;

@Getter
public class AppBusinessException extends  RuntimeException {
    private final String errorKey;
    public AppBusinessException(String message, String errorKey) {
        super(message);
        this.errorKey = errorKey;
    }
}
