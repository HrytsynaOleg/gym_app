package com.gym.dao;

import com.gym.model.TraineeModel;

public interface ITraineeDao {
    TraineeModel create(TraineeModel traineeModel);
    TraineeModel getByUserName(String userName);
    void update(TraineeModel traineeModel);
    void delete(long id);
    TraineeModel get(long id);
}
