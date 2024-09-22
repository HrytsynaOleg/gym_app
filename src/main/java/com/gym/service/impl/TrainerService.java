package com.gym.service.impl;

import com.gym.dao.ITrainerDao;
import com.gym.exceptions.IncorrectCredentialException;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.model.UserCredentials;
import com.gym.service.ITrainerService;
import com.gym.utils.StorageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.gym.utils.StringUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;

@Log4j2
@Service("trainerService")
public class TrainerService implements ITrainerService {
    @Autowired
    private ITrainerDao trainerDao;
    @Autowired
    private LocalValidatorFactoryBean localValidatorFactoryBean;
    @Value("${password.length}")
    private Integer passwordLength;

    @Override
    public TrainerModel createTrainer(String firstName, String lastName,
                                      String trainingTypeString) throws ValidationException {
        long usersCount = trainerDao.getUserCountByUserName(firstName, lastName);
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

        Validator validator = localValidatorFactoryBean.getValidator();
        Set<ConstraintViolation<TrainerModel>> validate = validator.validate(trainerModel);
        if (!validate.isEmpty()) {
            log.error("Trainer validation error");
            throw new ValidationException("Trainer validation failed");
        }

        TrainerModel newTrainerModel = trainerDao.create(trainerModel);
        log.info("New trainer created in  trainer service");
        return newTrainerModel;
    }

    @Override
    public boolean isCredentialsNotMatch(UserCredentials credentials) {
        TrainerModel trainer = trainerDao.getByUserName(credentials.getUserName());
        if (trainer == null) {
            return true;
        }
        return !trainer.getPassword().equals(credentials.getPassword());
    }

    @Override
    public TrainerModel getTrainerProfile(UserCredentials credentials) throws IncorrectCredentialException {
        if (isCredentialsNotMatch(credentials)){
            throw new IncorrectCredentialException("User name or password incorrect");
        }
        return trainerDao.getByUserName(credentials.getUserName());
    }

    @Override
    public void updateTrainerProfile(UserCredentials credentials, TrainerModel trainerModel) throws ValidationException,
            IncorrectCredentialException {
        if (isCredentialsNotMatch(credentials)){
            throw new IncorrectCredentialException("User name or password incorrect");
        }
        Set<ConstraintViolation<TrainerModel>> validate = localValidatorFactoryBean.getValidator().validate(trainerModel);
        if (!validate.isEmpty()) {
            log.error("Trainer profile validation error");
            throw new ValidationException("Trainer validation failed");
        }
        trainerDao.update(trainerModel);
    }

    @Override
    public void updateTrainerPassword(UserCredentials credentials, String password) throws IncorrectCredentialException {
        if (isCredentialsNotMatch(credentials)){
            throw new IncorrectCredentialException("User name or password incorrect");
        }
        TrainerModel trainer = trainerDao.getByUserName(credentials.getUserName());
        trainer.setPassword(password);
        trainerDao.update(trainer);
        log.info("Password changed");
    }

    @Override
    public TrainerModel get(long id) {
        return trainerDao.get(id);
    }
}
