package com.gym.dao.impl;

import com.gym.dao.ITrainingDao;
import com.gym.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import utils.StorageUtils;
import java.util.Map;

@Repository
public class TrainingDao implements ITrainingDao {
    @Autowired
    private Map<String, Training> storage;

    @Override
    public Training add(Training training) {
        long id = StorageUtils.generateId(storage);
        training.setId(id);
        storage.put(String.valueOf(id), training);
        return training;
    }

    @Override
    public Training getById(long id) {
        return storage.get(String.valueOf(id));
    }
}
