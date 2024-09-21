package com.gym.service.impl;

import com.gym.dao.ITrainerDao;
import com.gym.entity.User;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.service.ITrainerService;
import com.gym.utils.StorageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.gym.utils.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Log4j2
@Service
//@Validated
public class TrainerService implements ITrainerService {
    @Autowired
    private ITrainerDao trainerDao;
    @Autowired
    LocalValidatorFactoryBean localValidatorFactoryBean;
    @Value("${password.length}")
    private Integer passwordLength;

    @Override
    public TrainerModel createTrainer(String firstName, String lastName,
                                      String trainingTypeString) {
        long usersCount = trainerDao.getUserCountByUserName(firstName, lastName);
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

        Set<ConstraintViolation<TrainerModel>> validate = localValidatorFactoryBean.getValidator().validate(trainerModel);
        if (!validate.isEmpty()) {
            for (ConstraintViolation<TrainerModel> violation : validate) {
                System.out.println(violation.getMessage());
            }
            throw new ValidationException("Validation failed");
        } else {
            System.out.println("User is valid");
        }

        TrainerModel newTrainerModel = trainerDao.create(trainerModel);
        log.info("New trainer created in  trainer service");
        return newTrainerModel;
    }

    @Override
    public void updateTrainer(TrainerModel trainerModel) {
        trainerDao.update(trainerModel);
    }

    @Override
    public TrainerModel getById(long id) {
        return trainerDao.get(id);
    }
}
