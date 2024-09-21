package com.gym.service;

import com.gym.model.TrainerModel;

import javax.validation.constraints.NotBlank;

public interface ITrainerService {
    TrainerModel createTrainer(String firstName, String lastName, String trainingType);
    void updateTrainer(TrainerModel trainerModel);
    TrainerModel getById(long id);
}
