package com.revo.domain.exception;

public class AreaConfigurationException extends RuntimeException {
    private static final String MESSAGE = "Area is not configured!";

    public AreaConfigurationException() {
        super(MESSAGE);
    }
}
