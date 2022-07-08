package com.revo.domain.exception;

public class DatabaseException extends RuntimeException {
    private static final String MESSAGE = "Critical error in database, plugin may not works!";

    public DatabaseException() {
        super(MESSAGE);
    }
}
