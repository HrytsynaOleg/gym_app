package com.gym.dao;

import com.gym.model.Trainer;

import java.util.List;

public interface ITrainerDao {
    Trainer create(Trainer trainer);
    void update(Trainer trainer);
    Trainer get(long id);
    List<Trainer> getListByUserName(String userName);
}
