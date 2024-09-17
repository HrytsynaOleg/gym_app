package com.gym.service;

import com.gym.model.TraineeModel;


public interface ITraineeService {
    TraineeModel createTrainee(String firstName, String lastName, String address, String dateOfBirth);
    void update(TraineeModel traineeModel);
    void delete(long id);
    TraineeModel getById(long id);
}
