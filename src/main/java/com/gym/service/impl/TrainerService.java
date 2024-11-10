package com.gym.service.impl;

import com.gym.dao.ITraineeDao;
import com.gym.dao.ITrainerDao;
import com.gym.dao.ITrainingDao;
import com.gym.dao.IUserDao;
import com.gym.dto.training.TrainerTrainingListItemDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.*;
import com.gym.security.JwtTokenService;
import com.gym.service.IModelValidator;
import com.gym.service.ITrainerService;
import com.gym.utils.DTOMapper;
import com.gym.utils.DateUtils;
import com.gym.utils.StorageUtils;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.gym.utils.StringUtils;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.ValidationException;

import java.util.*;

@Log4j2
@Service("trainerService")
public class TrainerService implements ITrainerService {
    @Autowired
    private ITrainerDao trainerDao;
    @Autowired
    private ITrainingDao trainingDao;
    @Autowired
    private ITraineeDao traineeDao;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IModelValidator validator;
    @Autowired
    private JwtTokenService tokenService;
    @Value("${password.length}")
    private Integer passwordLength;
    private final Counter trainerRegistrationCounter;
    private final PasswordEncoder passwordEncoder;

    public TrainerService(MeterRegistry registry) {
        trainerRegistrationCounter = Counter.builder("trainer.register")
                .description("Number of trainer registered")
                .register(registry);
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    @Validated
    public TrainerModel createTrainer(String firstName, String lastName,
                                      String trainingTypeString) throws ValidationException {
        long usersCount = userDao.getUserCount(firstName, lastName);
        String userName = StorageUtils.generateUserName(firstName, lastName, usersCount);
        String generatedPassword = StringUtils.generateRandomString(passwordLength);
        String password = "{bcrypt}" + passwordEncoder.encode(generatedPassword);
        log.info(generatedPassword);
        log.info(password);
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
        String token = tokenService.generateToken(newTrainerModel);
        newTrainerModel.setToken(token);
        userDao.update(newTrainerModel);
        newTrainerModel.setPassword(generatedPassword);
        log.info("New trainer created in trainer service. Transaction Id {}", MDC.get("transactionId"));
        trainerRegistrationCounter.increment();
        return newTrainerModel;
    }

    @Override
    public TrainerModel getTrainerProfile(String username) throws NoSuchElementException {
        return trainerDao.getByUserName(username);
    }

    @Override
    public List<TrainerTrainingListItemDTO> getTrainingList(String username, String dateFrom, String dateTo,
                                                            String traineeUserName) throws IncorrectCredentialException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("trainer", username);
        if (!traineeUserName.isBlank()) {
            parameters.put("trainee", traineeUserName);
        }
        if (!dateFrom.isBlank() && !dateTo.isBlank()) {
            parameters.put("startDate", DateUtils.parseDate(dateFrom));
            parameters.put("endDate", DateUtils.parseDate(dateTo));
        }
        List<TrainingModel> trainingModelList = trainingDao.getTrainerTrainingListByParameters(parameters);
        List<TrainerTrainingListItemDTO> resultList = new ArrayList<>();
        for (TrainingModel trainingModel : trainingModelList) {
            TrainerTrainingListItemDTO trainerTrainingListItemDTO = DTOMapper.mapTrainingModelToTrainerTrainingItem(trainingModel);
            String traineeName = traineeDao.get(trainingModel.getTraineeId()).getUserName();
            trainerTrainingListItemDTO.setTraineeName(traineeName);
            resultList.add(trainerTrainingListItemDTO);
        }
        return resultList;
    }

    @Override
    public List<TrainerModel> getNotAssignedTrainerList(String username) {
        return trainerDao.getNotAssignedTrainerList();
    }

    @Override
    public void activate(String username) throws ValidationException,
            NoSuchElementException {
        setActiveStatus(username, true);
        log.info("User activated. Transaction Id {}", MDC.get("transactionId"));
    }

    @Override
    public void deactivate(String username) throws ValidationException, NoSuchElementException {
        setActiveStatus(username, false);
        log.info("User deactivated. Transaction Id {}", MDC.get("transactionId"));
    }

    @Override
    public TrainerModel updateTrainerProfile(String username, TrainerModel trainerModel) throws ValidationException,
            NoSuchElementException {
        trainerDao.getByUserName(username);
        validator.validate(trainerModel);
        trainerDao.update(trainerModel);
        TrainerModel updatedTrainerModel = trainerDao.getByUserName(username);
        log.info("Trainer profile updated. Transaction Id {}", MDC.get("transactionId"));
        return updatedTrainerModel;
    }

    @Override
    public TrainerModel get(long id) throws NoSuchElementException {
        return trainerDao.get(id);
    }

    @Override
    public List<TraineeModel> getAssignedTraineeList(String username)
            throws NoSuchElementException {
        TrainerModel trainer = trainerDao.getByUserName(username);
        return trainerDao.getAssignedTraineeList(trainer.getId());
    }

    private void setActiveStatus(String username, boolean status) throws ValidationException,
            NoSuchElementException {
        TrainerModel trainer = trainerDao.getByUserName(username);
        trainer.setIsActive(status);
        validator.validate(trainer);
        trainerDao.update(trainer);
    }
}
