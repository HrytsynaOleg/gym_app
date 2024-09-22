package com.gym.dao;

import com.gym.model.TraineeModel;

public interface ITraineeDao {
    TraineeModel add(TraineeModel traineeModel);
    void update(TraineeModel traineeModel);
    void delete(long id);
    TraineeModel getById(long id);
    long getUserCountByUserName(String firstName, String lastName);
}
