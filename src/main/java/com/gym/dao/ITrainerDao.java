package com.gym.dao;

import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;

import java.util.List;

public interface ITrainerDao {
    TrainerModel create(TrainerModel trainerModel);
    TrainerModel getByUserName(String username);
    void update(TrainerModel trainerModel);
    TrainerModel get(long id);
    List<TrainerModel> getNotAssignedTrainerList();
    List<TraineeModel> getAssignedTraineeList(long id);
}
