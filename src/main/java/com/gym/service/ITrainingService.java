package com.gym.service;

import com.gym.model.Training;
import com.gym.model.TrainingType;

import java.time.LocalDate;

public interface ITrainingService {
    Training createTraining(long traineeId, long trainerId, String trainingName, TrainingType trainingType,
                            LocalDate trainingDate, int duration);
    Training getById(long id);
}
