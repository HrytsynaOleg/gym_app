package com.gym.dao;

import com.gym.model.Trainer;

import java.util.List;

public interface ITrainerDao {
    Trainer add(Trainer trainer);
    void update(Trainer trainer);
    Trainer getById(long id);
    List<Trainer> getListByUserName(String userName);
}
