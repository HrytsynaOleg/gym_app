package com.gym.dao;

import com.gym.model.TrainingModel;

import java.util.List;
import java.util.Map;

public interface ITrainingDao {
    TrainingModel create(TrainingModel trainingModel);
    List<TrainingModel> getTrainerTrainingList(Map<String, Object> parameters);
    List<TrainingModel> getTraineeTrainingList(long traineeId);
    TrainingModel get(long id);
}
