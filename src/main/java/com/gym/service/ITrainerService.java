package com.gym.service;

import com.gym.dto.training.TrainerTrainingListItemDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import jakarta.validation.ValidationException;

import java.util.List;
import java.util.NoSuchElementException;

public interface ITrainerService {
    TrainerModel createTrainer(String firstName, String lastName, String trainingType);

    TrainerModel getTrainerProfile(String username) throws NoSuchElementException;

    List<TrainerTrainingListItemDTO> getTrainingList(String username, String dateFrom,
                                                     String dateTo, String traineeUserName) throws IncorrectCredentialException;

    List<TrainerModel> getNotAssignedTrainerList(String username);

    void activate(String username) throws ValidationException, NoSuchElementException;

    void deactivate(String username) throws ValidationException, NoSuchElementException;

    TrainerModel updateTrainerProfile(String username, TrainerModel trainerModel) throws ValidationException, NoSuchElementException;

    TrainerModel get(long id) throws NoSuchElementException;

    List<TraineeModel> getAssignedTraineeList(String username) throws NoSuchElementException;
}
