package com.gym.service;

import com.gym.exception.IncorrectCredentialException;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingModel;
import com.gym.model.UserCredentials;

import jakarta.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;


public interface ITraineeService {
    TraineeModel createTrainee(String firstName, String lastName, String address, String dateOfBirth);
    boolean isCredentialsNotMatch(UserCredentials credentials);
    TraineeModel getTraineeProfile(UserCredentials credentials) throws IncorrectCredentialException;
    void activate(UserCredentials credentials) throws ValidationException, IncorrectCredentialException;
    void deactivate(UserCredentials credentials) throws ValidationException, IncorrectCredentialException;
    void update(UserCredentials credentials, TraineeModel traineeModel) throws IncorrectCredentialException;
    void delete(UserCredentials credentials) throws IncorrectCredentialException;
    TraineeModel get(UserCredentials credentials, long id) throws IncorrectCredentialException;
    List<TrainingModel> getTrainingList(UserCredentials credentials, LocalDate dateFrom,
    LocalDate dateTo, String trainerUserName, int trainingType) throws IncorrectCredentialException;
    List<TrainerModel> getIntendedTrainerList(UserCredentials credentials)throws IncorrectCredentialException;
    void updateTrainerList(UserCredentials credentials, List<TrainerModel> trainerModelList) throws IncorrectCredentialException;
}
