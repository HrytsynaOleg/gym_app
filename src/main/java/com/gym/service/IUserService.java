package com.gym.service;

import com.gym.exception.IncorrectCredentialException;
import com.gym.model.UserCredentials;
import com.gym.model.UserModel;
import jakarta.validation.ValidationException;

public interface IUserService {
    UserModel getUserProfile(UserCredentials credentials) throws IncorrectCredentialException;
    void login(UserCredentials credentials) throws IncorrectCredentialException;
    void changePassword(UserCredentials credentials, String newPassword) throws ValidationException,
            IncorrectCredentialException;
}
