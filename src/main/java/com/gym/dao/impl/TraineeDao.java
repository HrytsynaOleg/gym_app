package com.gym.dao.impl;

import com.gym.dao.ITraineeDao;
import com.gym.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import utils.StorageUtils;

import java.util.List;
import java.util.Map;

@Repository
public class TraineeDao implements ITraineeDao {
    @Autowired
    private Map<String, Trainee> storage;

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
    public void delete(Trainee trainee) {
        storage.remove(String.valueOf(trainee.getId()));
    }

    @Override
    public Trainee getById(long id) {
        return storage.get(String.valueOf(id));
    }

    @Override
    public List<Trainee> getListByUserName(String userName) {
        return StorageUtils.findByUserName(storage, userName);
    }
}
