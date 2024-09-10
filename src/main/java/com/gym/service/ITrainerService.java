package com.gym.service;

import com.gym.model.Trainer;

public interface ITrainerService {
    Trainer createTrainer(String firstName, String lastName, String trainingType);
    void update(Trainer trainer);
    Trainer getById(long id);
}
