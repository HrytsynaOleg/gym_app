package com.gym.dao.impl;

import com.gym.dao.ITraineeDao;
import com.gym.model.TraineeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gym.utils.StorageUtils;

import java.util.Map;

@Repository
public class TraineeDao implements ITraineeDao {

    private final Map<String, TraineeModel> storage;

    @Autowired
    public TraineeDao(Map<String, TraineeModel> storage) {
        this.storage = storage;
    }

    @Override
    public TraineeModel add(TraineeModel traineeModel) {
        long id = StorageUtils.generateId(storage);
        traineeModel.setId(id);
        storage.put(String.valueOf(id), traineeModel);
        return traineeModel;
    }

    @Override
    public void update(TraineeModel traineeModel) {
        storage.put(String.valueOf(traineeModel.getId()), traineeModel);
    }

    @Override
    public void delete(long id) {
        storage.remove(String.valueOf(id));
    }

    @Override
    public TraineeModel getById(long id) {
        return storage.get(String.valueOf(id));
    }

    @Override
    public long getUserCountByUserName(String firstName, String lastName) {
        String userName = firstName + "." + lastName;
        return StorageUtils.getUserCountByUserName(storage, userName);
    }
}

