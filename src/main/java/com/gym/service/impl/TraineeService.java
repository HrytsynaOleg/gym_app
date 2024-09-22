package com.gym.service.impl;

import com.gym.dao.ITraineeDao;
import com.gym.dao.ITrainerDao;
import com.gym.model.TraineeModel;
import com.gym.service.ITraineeService;
import com.gym.utils.StorageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.gym.utils.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Log4j2
public class TraineeService implements ITraineeService {
    @Autowired
    private ITrainerDao trainerDao;
    @Autowired
    private ITraineeDao traineeDao;
    @Value("${password.length}")
    private Integer passwordLength;

    @Override
    public TraineeModel createTrainee(String firstName, String lastName, String address, String dateOfBirth) {
        long usersCount = trainerDao.getUserCountByUserName(firstName, lastName) +
                traineeDao.getUserCountByUserName(firstName, lastName);
        String userName = StorageUtils.generateUserName(firstName, lastName, usersCount);
        String password = StringUtils.generateRandomString(passwordLength);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateOfBirth, formatter);
        TraineeModel traineeModel = TraineeModel.builder()
                .id(0)
                .firstName(firstName)
                .lastName(lastName)
                .userName(userName)
                .password(password)
                .isActive(true)
                .address(address)
                .dateOfBirth(localDate)
                .build();
        TraineeModel newTraineeModel = traineeDao.add(traineeModel);
        log.info("New trainee created");
        return newTraineeModel;
    }

    @Override
    public void update(TraineeModel traineeModel) {
        traineeDao.update(traineeModel);
    }

    @Override
    public void delete(long id) {
        traineeDao.delete(id);
    }

    @Override
    public TraineeModel getById(long id) {
        return traineeDao.getById(id);
    }
}
