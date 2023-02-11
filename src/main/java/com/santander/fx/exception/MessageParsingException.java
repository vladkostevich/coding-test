package com.santander.fx.exception;

public class MessageParsingException extends Exception {

    public MessageParsingException(String message) {
        super(message);
    }

    public MessageParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
