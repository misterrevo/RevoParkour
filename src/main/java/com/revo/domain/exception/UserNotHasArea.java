package com.revo.domain.exception;

public class UserNotHasArea extends RuntimeException {
    private static final String MESSAGE = "Error while removing user from area, user don't have area!";

    public UserNotHasArea() {
        super(MESSAGE);
    }
}
