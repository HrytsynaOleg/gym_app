package com.gym.service;

import com.gym.exceptions.IncorrectCredentialException;
import com.gym.model.UserCredentials;

public interface IUserCredentialsService {
    void verifyCredentials(UserCredentials credentials) throws IncorrectCredentialException;
}
