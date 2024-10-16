package com.gym.service;

import com.gym.exception.IncorrectCredentialException;
import com.gym.model.UserCredentials;

public interface IUserCredentialsService {
    void verifyCredentials(UserCredentials credentials) throws IncorrectCredentialException;
}
