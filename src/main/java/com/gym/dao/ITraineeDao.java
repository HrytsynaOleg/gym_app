package com.gym.dao;

import com.gym.model.Trainee;

public interface ITraineeDao {
    Trainee add(Trainee trainee);
    void update(Trainee trainee);
    void delete(long id);
    Trainee getById(long id);
    long getUserCountByUserName(String firstName, String lastName);
}
