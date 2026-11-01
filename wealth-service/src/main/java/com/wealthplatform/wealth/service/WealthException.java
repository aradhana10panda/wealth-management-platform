package com.wealthplatform.wealth.service;

import org.springframework.http.HttpStatus;

public class WealthException extends RuntimeException {
    private final HttpStatus status;

    public WealthException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
