package com.santander.fx.exception;

public class MarketPriceNotFoundException extends RuntimeException {

    public MarketPriceNotFoundException() {
    }

    public MarketPriceNotFoundException(String message) {
        super(message);
    }

    public MarketPriceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
