package com.revo.domain.exception;

public class AreaNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Error while getting area, probably not exists in base!";

    public AreaNotFoundException() {
        super(MESSAGE);
    }
}
