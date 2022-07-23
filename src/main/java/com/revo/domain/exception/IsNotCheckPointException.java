package com.revo.domain.exception;

public class IsNotCheckPointException extends RuntimeException {
    private static final String MESSAGE = "Current location is not checkpoint in area!";

    public IsNotCheckPointException() {
        super(MESSAGE);
    }
}
