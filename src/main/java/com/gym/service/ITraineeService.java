package com.gym.service;

import com.gym.exceptions.IncorrectCredentialException;
import com.gym.model.TraineeModel;
import com.gym.model.UserCredentials;

import javax.validation.ValidationException;


public interface ITraineeService {
    TraineeModel createTrainee(String firstName, String lastName, String address, String dateOfBirth);
    boolean isCredentialsNotMatch(UserCredentials credentials);
    TraineeModel getTraineeProfile(UserCredentials credentials) throws IncorrectCredentialException;
    void activate(UserCredentials credentials) throws ValidationException, IncorrectCredentialException;
    void deactivate(UserCredentials credentials) throws ValidationException, IncorrectCredentialException;
    void updateTraineePassword(UserCredentials credentials, String password) throws IncorrectCredentialException;
    void update(UserCredentials credentials, TraineeModel traineeModel) throws IncorrectCredentialException;
    void delete(UserCredentials credentials) throws IncorrectCredentialException;
    TraineeModel get(long id);
}
