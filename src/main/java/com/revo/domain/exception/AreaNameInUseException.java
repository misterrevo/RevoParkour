package com.revo.domain.exception;

public class AreaNameInUseException extends RuntimeException {
    private static final String MESSAGE = "Error while creating area, name is in use!";

    public AreaNameInUseException() {
        super(MESSAGE);
    }
}
