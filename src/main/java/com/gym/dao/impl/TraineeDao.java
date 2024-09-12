package com.gym.dao.impl;

import com.gym.dao.ITraineeDao;
import com.gym.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gym.utils.StorageUtils;

import java.util.Map;

@Repository
public class TraineeDao implements ITraineeDao {

    private final Map<String, Trainee> storage;

    @Autowired
    public TraineeDao(Map<String, Trainee> storage) {
        this.storage = storage;
    }

    @Override
    public Trainee add(Trainee trainee) {
        long id = StorageUtils.generateId(storage);
        trainee.setId(id);
        storage.put(String.valueOf(id), trainee);
        return trainee;
    }

    @Override
    public void update(Trainee trainee) {
        storage.put(String.valueOf(trainee.getId()), trainee);
    }

    @Override
    public void delete(long id) {
        storage.remove(String.valueOf(id));
    }

    @Override
    public Trainee getById(long id) {
        return storage.get(String.valueOf(id));
    }

    @Override
    public long getUserCountByUserName(String firstName, String lastName) {
        String userName = firstName + "." + lastName;
        return StorageUtils.getUserCountByUserName(storage, userName);
    }
}

