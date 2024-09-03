package com.gym.service.impl;

import com.gym.dao.ITraineeDao;
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
    @Autowired
    ITraineeDao traineeDao;

    @Override
    public Trainer createTrainer(String firstName, String lastName, TrainingType trainingType) {
        String userName = firstName + "." + lastName;
        int usersCount = trainerDao.getListByUserName(userName).size() + traineeDao.getListByUserName(userName).size();
        if (usersCount > 0) {
            userName = userName + usersCount;
        }
        String password = StringUtils.generateRandomString(10);
        Trainer trainer = new Trainer();
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setTrainingType(trainingType);
        trainer.setPassword(password);
        trainer.setUserName(userName);
        trainer.setIsActive(true);
        return trainerDao.add(trainer);
    }

    @Override
    public void update(Trainer trainer) {
        trainerDao.update(trainer);
    }

    @Override
    public Trainer getById(long id) {
        return trainerDao.getById(id);
    }
}
