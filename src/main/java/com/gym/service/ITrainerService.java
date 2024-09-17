package com.gym.service;

import com.gym.model.TrainerModel;

public interface ITrainerService {
    TrainerModel createTrainer(String firstName, String lastName, String trainingType);
    void update(TrainerModel trainerModel);
    TrainerModel getById(long id);
}
