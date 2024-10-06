package com.gym.exception;

public class IncorrectCredentialException extends Exception {
    public IncorrectCredentialException(String errorMessage) {
        super(errorMessage);
    }
}
