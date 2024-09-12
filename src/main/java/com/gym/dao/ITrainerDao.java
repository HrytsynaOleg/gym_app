package com.gym.dao;

import com.gym.model.Trainer;

public interface ITrainerDao {
    Trainer create(Trainer trainer);
    void update(Trainer trainer);
    Trainer get(long id);
    long getUserCountByUserName(String firstName, String lastName);
}
