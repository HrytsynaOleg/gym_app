package com.gym.service;

import com.gym.model.Training;
import com.gym.model.TrainingType;

import java.util.Date;

public interface ITrainingService {
    Training createTraining(long traineeId, long trainerId, String trainingName, TrainingType trainingType,
                            Date trainingDate, int duration);
    Training getById(long id);
}
