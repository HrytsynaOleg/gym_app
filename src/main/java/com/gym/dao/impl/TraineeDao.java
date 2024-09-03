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
    Map<String, Trainee> storage;

    @Override
    public Trainee add(Trainee trainee) {
        return null;
    }

    @Override
    public void update(Trainee trainee) {

    }

    @Override
    public void delete(Trainee trainee) {

    }

    @Override
    public Trainee getById(long id) {
        return null;
    }

    @Override
    public List<Trainee> getListByUserName(String userName) {
        return StorageUtils.findByUserName(storage, userName);
    }
}
