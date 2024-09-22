package com.gym.dao;

import com.gym.model.TrainingModel;

public interface ITrainingDao {
    TrainingModel create(TrainingModel trainingModel);
    TrainingModel get(long id);
}
