package com.gym.service.impl;

import com.gym.dao.ITraineeDao;
import com.gym.dao.ITrainerDao;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.service.ITrainerService;
import com.gym.utils.StorageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.gym.utils.StringUtils;

@Log4j2
@Service
public class TrainerService implements ITrainerService {
    @Autowired
    private ITrainerDao trainerDao;
    @Autowired
    private ITraineeDao traineeDao;
    @Value("${password.length}")
    private Integer passwordLength;

    @Override
    public TrainerModel createTrainer(String firstName, String lastName, String trainingTypeString) {
        long usersCount = trainerDao.getUserCountByUserName(firstName, lastName) +
                traineeDao.getUserCountByUserName(firstName, lastName);
        String userName = StorageUtils.generateUserName(firstName, lastName, usersCount);
        String password = StringUtils.generateRandomString(passwordLength);
        TrainingTypeEnum trainingTypeEnum = TrainingTypeEnum.valueOf(trainingTypeString);
        TrainerModel trainerModel = TrainerModel.builder()
                .id(0)
                .firstName(firstName)
                .lastName(lastName)
                .userName(userName)
                .password(password)
                .isActive(true)
                .trainingType(trainingTypeEnum)
                .build();
        TrainerModel newTrainerModel = trainerDao.create(trainerModel);
        log.info("New trainer created");
        return newTrainerModel;
    }

    @Override
    public void update(TrainerModel trainerModel) {
        trainerDao.update(trainerModel);
    }

    @Override
    public TrainerModel getById(long id) {
        return trainerDao.get(id);
    }
}
