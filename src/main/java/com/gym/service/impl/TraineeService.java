package com.gym.service.impl;

import com.gym.dao.ITraineeDao;
import com.gym.dao.ITrainingDao;
import com.gym.dao.IUserDao;
import com.gym.exceptions.IncorrectCredentialException;
import com.gym.model.TraineeModel;
import com.gym.model.UserCredentials;
import com.gym.service.IModelValidator;
import com.gym.service.ITraineeService;
import com.gym.service.IUserCredentialsService;
import com.gym.utils.StorageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.gym.utils.StringUtils;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service("traineeService")
@Log4j2
public class TraineeService implements ITraineeService {
    @Autowired
    private ITraineeDao traineeDao;
    @Autowired
    private ITrainingDao trainingDao;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IModelValidator validator;
    @Autowired
    private IUserCredentialsService credentialsService;
    @Value("${password.length}")
    private Integer passwordLength;
    @Value("${date.format}")
    private String dateFormat;

    @Override
    public TraineeModel createTrainee(String firstName, String lastName, String address, String dateOfBirth) {
        long usersCount = userDao.getUserCount(firstName, lastName);
        String userName = StorageUtils.generateUserName(firstName, lastName, usersCount);
        String password = StringUtils.generateRandomString(passwordLength);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
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
        validator.validate(traineeModel);
        TraineeModel newTraineeModel = traineeDao.add(traineeModel);
        log.info("New trainee created");
        return newTraineeModel;
    }

    @Override
    public boolean isCredentialsNotMatch(UserCredentials credentials) {
        try {
            credentialsService.verifyCredentials(credentials);
        } catch (IncorrectCredentialException e) {
            return true;
        }
        return false;
    }

    @Override
    public TraineeModel getTraineeProfile(UserCredentials credentials) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        return traineeDao.getByUserName(credentials.getUserName());
    }

    @Override
    public void activate(UserCredentials credentials) throws ValidationException,
            IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        setActiveStatus(credentials, true);
    }

    @Override
    public void deactivate(UserCredentials credentials) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        setActiveStatus(credentials, false);
    }

    @Override
    public void updateTraineePassword(UserCredentials credentials, String password) throws ValidationException,
            IncorrectCredentialException{
        credentialsService.verifyCredentials(credentials);
        TraineeModel trainee = traineeDao.getByUserName(credentials.getUserName());
        trainee.setPassword(password);
        validator.validate(trainee);
        traineeDao.update(trainee);
        log.info("Password changed");
    }

    @Override
    public void update(UserCredentials credentials, TraineeModel traineeModel) throws IncorrectCredentialException{
        credentialsService.verifyCredentials(credentials);
        traineeDao.update(traineeModel);
    }

    @Override
    public void delete(UserCredentials credentials) throws IncorrectCredentialException{
        credentialsService.verifyCredentials(credentials);
        TraineeModel traineeModel = traineeDao.getByUserName(credentials.getUserName());
        traineeDao.delete(traineeModel.getId());
    }

    @Override
    public TraineeModel get(long id) {
        return traineeDao.get(id);
    }

    private void setActiveStatus(UserCredentials credentials, boolean status) throws ValidationException,
            IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        TraineeModel trainee = traineeDao.getByUserName(credentials.getUserName());
        trainee.setIsActive(status);
        validator.validate(trainee);
        traineeDao.update(trainee);
    }
}
