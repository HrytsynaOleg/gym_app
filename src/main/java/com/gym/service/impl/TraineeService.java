package com.gym.service.impl;

import com.gym.dao.ITraineeDao;
import com.gym.dao.ITrainerDao;
import com.gym.model.Trainee;
import com.gym.service.ITraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.StringUtils;

@Service
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
        Trainee trainee = new Trainee();
        trainee.setFirstName(firstName);
        trainee.setLastName(lastName);
        trainee.setPassword(password);
        trainee.setUserName(userName);
        trainee.setIsActive(true);
        trainee.setAddress(address);
        trainee.setDateOfBirth(dateOfBirth);
        return traineeDao.add(trainee);
    }

    @Override
    public void update(Trainee trainee) {
        traineeDao.update(trainee);
    }

    @Override
    public void delete(Trainee trainee) {
        traineeDao.delete(trainee);
    }

    @Override
    public Trainee getById(long id) {
        return traineeDao.getById(id);
    }
}
