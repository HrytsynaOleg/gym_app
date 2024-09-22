package com.gym.exceptions;

public class IncorrectCredentialException extends Exception {
    public IncorrectCredentialException(String errorMessage) {
        super(errorMessage);
    }
}
