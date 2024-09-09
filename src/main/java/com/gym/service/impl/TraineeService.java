package com.gym.service.impl;

import com.gym.dao.ITraineeDao;
import com.gym.dao.ITrainerDao;
import com.gym.model.Trainee;
import com.gym.service.ITraineeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public Trainee createTrainee(String firstName, String lastName, String address, String dateOfBirth) {
        String userName = firstName + "." + lastName;
        int usersCount = trainerDao.getListByUserName(userName).size() + traineeDao.getListByUserName(userName).size();
        if (usersCount > 0) {
            userName = userName + usersCount;
        }
        String password = StringUtils.generateRandomString(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateOfBirth, formatter);
        Trainee trainee = Trainee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .userName(userName)
                .password(password)
                .isActive(true)
                .address(address)
                .dateOfBirth(localDate)
                .build();
        Trainee newTrainee = traineeDao.add(trainee);
        log.info("New trainee created");
        return newTrainee;
    }

    @Override
    public void update(Trainee trainee) {
        traineeDao.update(trainee);
    }

    @Override
    public void delete(long id) {
        traineeDao.delete(id);
    }

    @Override
    public Trainee getById(long id) {
        return traineeDao.getById(id);
    }
}
