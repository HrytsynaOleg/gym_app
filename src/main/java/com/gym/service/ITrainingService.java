package com.gym.service;

import com.gym.model.TrainingModel;

public interface ITrainingService {
    TrainingModel createTraining(long traineeId, long trainerId, String trainingName, String trainingType,
                                 String trainingDate, int duration);
    TrainingModel getById(long id);
}
