package com.gym.dao;

import com.gym.model.Trainer;

public interface ITrainerDao {
    long addNew(Trainer trainer);
    void update(Trainer trainer);
    Trainer getById(long id);
}
