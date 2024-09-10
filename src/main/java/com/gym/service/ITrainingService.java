package com.gym.service;

import com.gym.model.Training;

public interface ITrainingService {
    Training createTraining(long traineeId, long trainerId, String trainingName, String trainingType,
                            String trainingDate, int duration);
    Training getById(long id);
}
