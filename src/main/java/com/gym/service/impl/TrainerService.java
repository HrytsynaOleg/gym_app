package com.gym.service.impl;

import com.gym.dao.ITraineeDao;
import com.gym.dao.ITrainerDao;
import com.gym.model.Trainer;
import com.gym.model.TrainingType;
import com.gym.service.ITrainerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gym.utils.StringUtils;

@Log4j2
@Service
public class TrainerService implements ITrainerService {
    @Autowired
    private ITrainerDao trainerDao;
    @Autowired
    private ITraineeDao traineeDao;

    @Override
    public Trainer createTrainer(String firstName, String lastName, String trainingTypeString) {
        String userName = firstName + "." + lastName;
        int usersCount = trainerDao.getListByUserName(userName).size() + traineeDao.getListByUserName(userName).size();
        if (usersCount > 0) {
            userName = userName + usersCount;
        }
        String password = StringUtils.generateRandomString(10);
        TrainingType trainingType = TrainingType.valueOf(trainingTypeString);
        Trainer trainer = Trainer.builder()
                .id(0)
                .firstName(firstName)
                .lastName(lastName)
                .userName(userName)
                .password(password)
                .isActive(true)
                .trainingType(trainingType)
                .build();
        Trainer newTrainer = trainerDao.create(trainer);
        log.info("New trainer created");
        return newTrainer;
    }

    @Override
    public void update(Trainer trainer) {
        trainerDao.update(trainer);
    }

    @Override
    public Trainer getById(long id) {
        return trainerDao.get(id);
    }
}
