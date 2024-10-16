package com.gym.dao;

import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;

import java.util.List;

public interface ITraineeDao {
    TraineeModel create(TraineeModel traineeModel);
    TraineeModel getByUserName(String userName);
    void update(TraineeModel traineeModel);
    void delete(long id);
    TraineeModel get(long id);
    void intendTrainer(TraineeModel traineeModel, TrainerModel trainerModel);
    void deleteTrainer(TraineeModel traineeModel, TrainerModel trainerModel);
    List<TrainerModel> getAssignedTrainerList(TraineeModel traineeModel);
    List<TrainerModel> getNotAssignedTrainerList(TraineeModel traineeModel);
}
