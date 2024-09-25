package com.gym.dao;

import com.gym.model.TrainingModel;

import java.util.List;
import java.util.Map;

public interface ITrainingDao {
    TrainingModel create(TrainingModel trainingModel);
    List<TrainingModel> getTrainerTrainingListByParameters(Map<String, Object> parameters);
    List<TrainingModel> getTraineeTrainingListById(long traineeId);
    List<TrainingModel> getTraineeTrainingListByParameters(Map<String, Object> parameters);
    TrainingModel get(long id);
}
