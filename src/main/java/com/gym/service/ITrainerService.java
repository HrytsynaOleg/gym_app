package com.gym.service;

import com.gym.model.Trainer;
import com.gym.model.TrainingType;

public interface ITrainerService {
    Trainer createTrainer(String firstName, String lastName, TrainingType trainingType);
    void update(Trainer trainer);
    Trainer getById(long id);
}
