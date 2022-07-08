package com.revo.domain.exception;

public class DatabaseException extends RuntimeException {
    private static final String MESSAGE = "Critical file system error in database!";

    public DatabaseException() {
        super(MESSAGE);
    }
}
