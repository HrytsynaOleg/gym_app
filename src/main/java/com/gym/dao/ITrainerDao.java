package com.gym.dao;

import com.gym.model.TrainerModel;

public interface ITrainerDao {
    TrainerModel create(TrainerModel trainerModel);
    void update(TrainerModel trainerModel);
    TrainerModel get(long id);
    long getUserCountByUserName(String firstName, String lastName);
}
