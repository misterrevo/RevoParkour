package com.revo.domain.exception;

public class UserHasNotArea extends RuntimeException {
    private static final String MESSAGE = "Error while removing user from area, user don't have area!";

    public UserHasNotArea() {
        super(MESSAGE);
    }
}
