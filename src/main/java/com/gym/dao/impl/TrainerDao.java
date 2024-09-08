package com.gym.dao.impl;

import com.gym.dao.ITrainerDao;
import com.gym.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gym.utils.StorageUtils;

import java.util.List;
import java.util.Map;

@Repository
public class TrainerDao implements ITrainerDao {
    @Autowired
    private Map<String, Trainer> storage;

    @Override
    public Trainer add(Trainer trainer) {
        long id = StorageUtils.generateId(storage);
        trainer.setId(id);
        storage.put(String.valueOf(id), trainer);
        return trainer;
    }

    @Override
    public void update(Trainer trainer) {
        storage.put(String.valueOf(trainer.getId()), trainer);
    }

    @Override
    public Trainer getById(long id) {
        return storage.get(String.valueOf(id));
    }

    @Override
    public List<Trainer> getListByUserName(String userName) {
        return StorageUtils.findByUserName(storage, userName);
    }
}
