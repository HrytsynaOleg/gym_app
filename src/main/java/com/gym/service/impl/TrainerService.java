package com.gym.service.impl;

import com.gym.dao.ITraineeDao;
import com.gym.dao.ITrainerDao;
import com.gym.dao.ITrainingDao;
import com.gym.dao.IUserDao;
import com.gym.dto.training.TrainerTrainingListItemDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.*;
import com.gym.service.IModelValidator;
import com.gym.service.ITrainerService;
import com.gym.service.IUserCredentialsService;
import com.gym.utils.DTOMapper;
import com.gym.utils.DateUtils;
import com.gym.utils.StorageUtils;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.gym.utils.StringUtils;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.ValidationException;
import java.util.ArrayList;
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
    private ITraineeDao traineeDao;
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
        log.info("New trainer created in trainer service. Transaction Id {}", MDC.get("transactionId"));
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
    public List<TrainerTrainingListItemDTO> getTrainingList(UserCredentials credentials, String dateFrom, String dateTo,
                                                            String traineeUserName) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("trainer", credentials.getUserName());
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
    public List<TrainerModel> getNotAssignedTrainerList(UserCredentials credentials) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        return trainerDao.getNotAssignedTrainerList();
    }

    @Override
    public void activate(UserCredentials credentials) throws ValidationException,
            IncorrectCredentialException {
        setActiveStatus(credentials, true);
        log.info("User activated. Transaction Id {}", MDC.get("transactionId"));
    }

    @Override
    public void deactivate(UserCredentials credentials) throws IncorrectCredentialException {
        setActiveStatus(credentials, false);
        log.info("User deactivated. Transaction Id {}", MDC.get("transactionId"));
    }

    @Override
    public TrainerModel updateTrainerProfile(UserCredentials credentials, TrainerModel trainerModel) throws ValidationException,
            IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        validator.validate(trainerModel);
        trainerDao.update(trainerModel);
        TrainerModel updatedTrainerModel = trainerDao.getByUserName(credentials.getUserName());
        log.info("Trainer profile updated. Transaction Id {}", MDC.get("transactionId"));
        return updatedTrainerModel;
    }

    @Override
    public TrainerModel get(UserCredentials credentials, long id) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        return trainerDao.get(id);
    }

    @Override
    public List<TraineeModel> getAssignedTraineeList(UserCredentials credentials)
            throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        TrainerModel trainer = trainerDao.getByUserName(credentials.getUserName());
        return trainerDao.getAssignedTraineeList(trainer.getId());
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
