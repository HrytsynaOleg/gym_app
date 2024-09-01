package com.gym.service.impl;

import com.gym.dao.ITrainerDao;
import com.gym.model.Trainer;
import com.gym.model.TrainingType;
import com.gym.service.ITrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.StringUtils;

@Service
public class TrainerService implements ITrainerService {
    @Autowired
    ITrainerDao trainerDao;

    @Override
    public long create(String firstName, String lastName, TrainingType trainingType) {
        Trainer trainer = new Trainer();
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setTrainingType(trainingType);
        String userName = firstName + "." + lastName;
        String password = StringUtils.generateRandomString(10);
        trainer.setPassword(password);
        trainer.setUserName(userName);
        trainer.setIsActive(true);
        return trainerDao.addNew(trainer);
    }

    @Override
    public void update(Trainer trainer) {

    }

    @Override
    public Trainer getById(long id) {
        return trainerDao.getById(id);
    }
}
