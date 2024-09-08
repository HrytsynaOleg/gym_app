package com.gym.dao;

import com.gym.model.Trainee;

import java.util.List;

public interface ITraineeDao {
    Trainee add(Trainee trainee);
    void update(Trainee trainee);
    void delete(long id);
    Trainee getById(long id);
    List<Trainee> getListByUserName(String userName);
}
