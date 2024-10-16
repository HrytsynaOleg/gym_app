package com.gym.service;

import com.gym.dto.training.TrainingCreateDTO;
import com.gym.model.TrainingModel;

public interface ITrainingService {
    TrainingModel createTraining(TrainingCreateDTO trainingCreateDTO);
    TrainingModel getById(long id);
}
