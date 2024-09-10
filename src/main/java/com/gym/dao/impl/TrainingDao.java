package com.gym.dao.impl;

import com.gym.dao.ITrainingDao;
import com.gym.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gym.utils.StorageUtils;
import java.util.Map;

@Repository
public class TrainingDao implements ITrainingDao {

    private final Map<String, Training> storage;

    @Autowired
    public TrainingDao(Map<String, Training> storage) {
        this.storage = storage;
    }

    @Override
    public Training create(Training training) {
        long id = StorageUtils.generateId(storage);
        training.setId(id);
        storage.put(String.valueOf(id), training);
        return training;
    }

    @Override
    public Training get(long id) {
        return storage.get(String.valueOf(id));
    }
}
