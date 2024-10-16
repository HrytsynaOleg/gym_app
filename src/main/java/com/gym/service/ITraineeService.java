package com.gym.service;

import com.gym.dto.training.TraineeTrainingListItemDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.model.UserCredentials;

import jakarta.validation.ValidationException;
import java.util.List;


public interface ITraineeService {
    TraineeModel createTrainee(String firstName, String lastName, String address, String dateOfBirth);
    boolean isCredentialsNotMatch(UserCredentials credentials);
    TraineeModel getTraineeProfile(UserCredentials credentials) throws IncorrectCredentialException;
    void activate(UserCredentials credentials) throws ValidationException, IncorrectCredentialException;
    void deactivate(UserCredentials credentials) throws ValidationException, IncorrectCredentialException;
    TraineeModel update(UserCredentials credentials, TraineeModel traineeModel) throws IncorrectCredentialException;
    void delete(UserCredentials credentials) throws IncorrectCredentialException;
    TraineeModel get(UserCredentials credentials, long id) throws IncorrectCredentialException;
    List<TraineeTrainingListItemDTO> getTrainingList(UserCredentials credentials, String dateFrom,
                                                     String dateTo, String trainerUserName, String trainingType) throws IncorrectCredentialException;
    List<TrainerModel> getAssignedTrainerList(UserCredentials credentials)throws IncorrectCredentialException;
    List<TrainerModel> getNotAssignedTrainerList(UserCredentials credentials)throws IncorrectCredentialException;
    List<TrainerModel> updateTrainerList(UserCredentials credentials, List<String> trainerModelList) throws IncorrectCredentialException;
}
