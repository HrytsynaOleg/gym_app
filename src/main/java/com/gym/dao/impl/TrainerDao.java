package com.gym.dao.impl;

import com.gym.dao.ITrainerDao;
import com.gym.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;

@Repository
public class TrainerDao implements ITrainerDao {
    @Autowired
    Map<String, Trainer> storage;

    @Override
    public long addNew(Trainer trainer) {
        Set<String> keys = storage.keySet();
        long maxValue = keys.stream()
                .mapToLong(Long::parseLong)
                .max()
                .orElse(0);
        long id = maxValue + 1;
        trainer.setId(id);
        storage.put(String.valueOf(id), trainer);
        return id;
    }

    @Override
    public void update(Trainer trainer) {

    }

    @Override
    public Trainer getById(long id) {
        return storage.get(String.valueOf(id));
    }
}
