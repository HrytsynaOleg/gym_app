package com.gym.service;

import com.gym.dto.training.TraineeTrainingListItemDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.model.UserCredentials;

import jakarta.validation.ValidationException;

import java.util.List;
import java.util.NoSuchElementException;


public interface ITraineeService {
    TraineeModel createTrainee(String firstName, String lastName, String address, String dateOfBirth);

    boolean isCredentialsNotMatch(UserCredentials credentials);

    TraineeModel getTraineeProfile(String username) throws NoSuchElementException;

    void activate(String username) throws ValidationException, NoSuchElementException;

    void deactivate(String username) throws ValidationException, NoSuchElementException;

    TraineeModel update(String username, TraineeModel traineeModel) throws NoSuchElementException;

    void delete(String username) throws NoSuchElementException;

    TraineeModel get(UserCredentials credentials, long id) throws IncorrectCredentialException;

    List<TraineeTrainingListItemDTO> getTrainingList(String username, String dateFrom,
                                                     String dateTo, String trainerUserName, String trainingType)
            throws NoSuchElementException;

    List<TrainerModel> getAssignedTrainerList(String username) throws NoSuchElementException;

    List<TrainerModel> getNotAssignedTrainerList(String username) throws NoSuchElementException;

    List<TrainerModel> updateTrainerList(String username, List<String> trainerModelList) throws NoSuchElementException;
}
