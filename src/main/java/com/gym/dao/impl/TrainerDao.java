package com.gym.dao.impl;

import com.gym.dao.ITrainerDao;
import com.gym.model.TrainerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gym.utils.StorageUtils;

import java.util.Map;

@Repository
public class TrainerDao implements ITrainerDao {

    private final Map<String, TrainerModel> storage;

    @Autowired
    public TrainerDao(Map<String, TrainerModel> storage) {
        this.storage = storage;
    }

    @Override
    public TrainerModel create(TrainerModel trainerModel) {
        long id = StorageUtils.generateId(storage);
        trainerModel.setId(id);
        storage.put(String.valueOf(id), trainerModel);
        return trainerModel;
    }

    @Override
    public void update(TrainerModel trainerModel) {
        storage.put(String.valueOf(trainerModel.getId()), trainerModel);
    }

    @Override
    public TrainerModel get(long id) {
        return storage.get(String.valueOf(id));
    }

    @Override
    public long getUserCountByUserName(String firstName, String lastName) {
        String userName = firstName + "." + lastName;
        return StorageUtils.getUserCountByUserName(storage, userName);
    }
}
