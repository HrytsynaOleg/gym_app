package com.gym.service;

import com.gym.exception.IncorrectCredentialException;
import com.gym.model.UserCredentials;
import com.gym.model.UserModel;
import jakarta.validation.ValidationException;

import java.util.NoSuchElementException;

public interface IUserService {
    UserModel getUserProfile(String username) throws IncorrectCredentialException;
    void login(UserCredentials credentials) throws IncorrectCredentialException;
    void changePassword(String username, String newPassword) throws ValidationException,
            NoSuchElementException;
}
