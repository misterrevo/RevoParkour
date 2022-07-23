package com.revo.domain.exception;

public class UserHasAreaException extends RuntimeException {
    private static final String MESSAGE = "Error while adding user to area, user have area!";

    public UserHasAreaException() {
        super(MESSAGE);
    }
}
