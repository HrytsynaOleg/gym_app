package com.gym.service;

import com.gym.exceptions.IncorrectCredentialException;
import com.gym.model.TrainerModel;
import com.gym.model.UserCredentials;

public interface ITrainerService {
    TrainerModel createTrainer(String firstName, String lastName, String trainingType);

    boolean isCredentialsNotMatch(UserCredentials credentials);

    TrainerModel getTrainerProfile(UserCredentials credentials) throws IncorrectCredentialException;

    void updateTrainerProfile(UserCredentials credentials, TrainerModel trainerModel) throws IncorrectCredentialException;

    void updateTrainerPassword(UserCredentials credentials, String password) throws IncorrectCredentialException;

    TrainerModel get(long id);
}
