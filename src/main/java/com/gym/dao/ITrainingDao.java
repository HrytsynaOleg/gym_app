package com.gym.dao;

import com.gym.model.Training;

public interface ITrainingDao {
    Training create(Training training);
    Training get(long id);
}
