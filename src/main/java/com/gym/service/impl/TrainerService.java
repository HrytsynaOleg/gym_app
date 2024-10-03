package com.gym.service.impl;

import com.gym.dao.ITrainerDao;
import com.gym.dao.ITrainingDao;
import com.gym.dao.IUserDao;
import com.gym.exceptions.IncorrectCredentialException;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.model.UserCredentials;
import com.gym.service.IModelValidator;
import com.gym.service.ITrainerService;
import com.gym.service.IUserCredentialsService;
import com.gym.utils.DateUtils;
import com.gym.utils.StorageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.gym.utils.StringUtils;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.ValidationException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service("trainerService")
public class TrainerService implements ITrainerService {
    @Autowired
    private ITrainerDao trainerDao;
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

    @Override
    @Validated
    public TrainerModel createTrainer(String firstName, String lastName,
                                      String trainingTypeString) throws ValidationException {
        long usersCount = userDao.getUserCount(firstName, lastName);
        String userName = StorageUtils.generateUserName(firstName, lastName, usersCount);
        String password = StringUtils.generateRandomString(passwordLength);
        TrainingTypeEnum trainingTypeEnum = TrainingTypeEnum.valueOf(trainingTypeString);
        TrainerModel trainerModel = TrainerModel.builder()
                .id(0)
                .userId(0)
                .firstName(firstName)
                .lastName(lastName)
                .userName(userName)
                .password(password)
                .isActive(true)
                .trainingType(trainingTypeEnum)
                .build();

        validator.validate(trainerModel);

        TrainerModel newTrainerModel = trainerDao.create(trainerModel);
        log.info("New trainer created in  trainer service");
        return newTrainerModel;
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
    public TrainerModel getTrainerProfile(UserCredentials credentials) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        return trainerDao.getByUserName(credentials.getUserName());
    }

    @Override
    public List<TrainingModel> getTrainingList(UserCredentials credentials, LocalDate dateFrom, LocalDate dateTo,
                                               String traineeUserName) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("trainer", credentials.getUserName());
        parameters.put("trainee", traineeUserName);
        parameters.put("startDate", DateUtils.localDateToDate(dateFrom));
        parameters.put("endDate", DateUtils.localDateToDate(dateTo));
        return trainingDao.getTrainerTrainingListByParameters(parameters);
    }

    @Override
    public List<TrainerModel> getNotAssignedTrainerList(UserCredentials credentials) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        return trainerDao.getNotAssignedTrainerList();
    }

    @Override
    public void activate(UserCredentials credentials) throws ValidationException,
            IncorrectCredentialException {
        setActiveStatus(credentials, true);
    }

    @Override
    public void deactivate(UserCredentials credentials) throws IncorrectCredentialException {
        setActiveStatus(credentials, false);
    }

    @Override
    public void updateTrainerProfile(UserCredentials credentials, TrainerModel trainerModel) throws ValidationException,
            IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        validator.validate(trainerModel);
        trainerDao.update(trainerModel);
    }

    @Override
    public void updateTrainerPassword(UserCredentials credentials, String password) throws ValidationException,
            IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        TrainerModel trainer = trainerDao.getByUserName(credentials.getUserName());
        trainer.setPassword(password);
        validator.validate(trainer);
        trainerDao.update(trainer);
        log.info("Password changed");
    }

    @Override
    public TrainerModel get(UserCredentials credentials, long id) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        return trainerDao.get(id);
    }

    private void setActiveStatus(UserCredentials credentials, boolean status) throws ValidationException,
            IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        TrainerModel trainer = trainerDao.getByUserName(credentials.getUserName());
        trainer.setIsActive(status);
        validator.validate(trainer);
        trainerDao.update(trainer);
    }
}
