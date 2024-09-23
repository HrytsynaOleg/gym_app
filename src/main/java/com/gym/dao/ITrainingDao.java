package com.gym.dao;

import com.gym.model.TrainingModel;

import java.util.List;
import java.util.Map;

public interface ITrainingDao {
    TrainingModel create(TrainingModel trainingModel);
    List<TrainingModel> getTrainerTrainingList(Map<String, Object> parameters);
    TrainingModel get(long id);
}
