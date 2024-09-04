package com.gym.service;

import com.gym.model.Trainee;

public interface ITraineeService {
    Trainee createTrainee(String firstName, String lastName, String address, String dateOfBirth);
    void update(Trainee trainee);
    void delete(Trainee trainee);
    Trainee getById(long id);
}
