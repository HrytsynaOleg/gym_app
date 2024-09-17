package com.gym.dao.impl;

import com.gym.dao.ITrainingDao;
import com.gym.model.TrainingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gym.utils.StorageUtils;
import java.util.Map;

@Repository
public class TrainingDao implements ITrainingDao {

    private final Map<String, TrainingModel> storage;

    @Autowired
    public TrainingDao(Map<String, TrainingModel> storage) {
        this.storage = storage;
    }

    @Override
    public TrainingModel create(TrainingModel trainingModel) {
        long id = StorageUtils.generateId(storage);
        trainingModel.setId(id);
        storage.put(String.valueOf(id), trainingModel);
        return trainingModel;
    }

    @Override
    public TrainingModel get(long id) {
        return storage.get(String.valueOf(id));
    }
}
