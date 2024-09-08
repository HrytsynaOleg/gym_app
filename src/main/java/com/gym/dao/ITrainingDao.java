package com.gym.dao;

import com.gym.model.Training;

public interface ITrainingDao {
    Training add(Training training);
    Training getById(long id);
}
