package com.vtt.exception;

public class ExceedsDurationLimitException extends RuntimeException {
    public ExceedsDurationLimitException(String message) {
        super(message);
    }
}
