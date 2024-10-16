package com.gym.service;

import com.gym.dto.training.TrainerTrainingListItemDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.model.UserCredentials;

import java.time.LocalDate;
import java.util.List;

public interface ITrainerService {
    TrainerModel createTrainer(String firstName, String lastName, String trainingType);

    boolean isCredentialsNotMatch(UserCredentials credentials);

    TrainerModel getTrainerProfile(UserCredentials credentials) throws IncorrectCredentialException;

    List<TrainerTrainingListItemDTO> getTrainingList(UserCredentials credentials, String dateFrom,
                                                     String dateTo, String traineeUserName) throws IncorrectCredentialException;
    List<TrainerModel> getNotAssignedTrainerList(UserCredentials credentials) throws IncorrectCredentialException;

    void activate(UserCredentials credentials) throws IncorrectCredentialException;

    void deactivate(UserCredentials credentials) throws IncorrectCredentialException;

    TrainerModel updateTrainerProfile(UserCredentials credentials, TrainerModel trainerModel) throws IncorrectCredentialException;

    TrainerModel get(UserCredentials credentials, long id) throws IncorrectCredentialException;

    List<TraineeModel> getAssignedTraineeList(UserCredentials credentials) throws IncorrectCredentialException;
}
