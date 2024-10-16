package com.gym.service.impl;

import com.gym.dao.ITraineeDao;
import com.gym.dao.ITrainerDao;
import com.gym.dao.ITrainingDao;
import com.gym.dao.IUserDao;
import com.gym.dto.training.TraineeTrainingListItemDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.*;
import com.gym.service.IModelValidator;
import com.gym.service.ITraineeService;
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

import jakarta.validation.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("traineeService")
@Log4j2
public class TraineeService implements ITraineeService {
    @Autowired
    private ITraineeDao traineeDao;
    @Autowired
    private ITrainingDao trainingDao;
    @Autowired
    private ITrainerDao trainerDao;
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
        TraineeModel traineeModel = TraineeModel.builder().id(0).firstName(firstName).lastName(lastName).userName(userName).password(password).isActive(true).address(address).dateOfBirth(localDate).build();
        validator.validate(traineeModel);
        TraineeModel newTraineeModel = traineeDao.create(traineeModel);
        log.info("New trainee created in trainee service. Transaction Id {}", MDC.get("transactionId"));
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
    public void activate(UserCredentials credentials) throws ValidationException, IncorrectCredentialException {
        setActiveStatus(credentials, true);
        log.info("User activated. Transaction Id {}", MDC.get("transactionId"));
    }

    @Override
    public void deactivate(UserCredentials credentials) throws IncorrectCredentialException {
        setActiveStatus(credentials, false);
        log.info("User deactivated. Transaction Id {}", MDC.get("transactionId"));
    }

    @Override
    public TraineeModel update(UserCredentials credentials, TraineeModel traineeModel) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        traineeDao.update(traineeModel);
        TraineeModel updatedTraineeModel = traineeDao.getByUserName(credentials.getUserName());
        log.info("Trainee profile updated. Transaction Id {}", MDC.get("transactionId"));
        return updatedTraineeModel;
    }

    @Override
    public void delete(UserCredentials credentials) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        TraineeModel traineeModel = traineeDao.getByUserName(credentials.getUserName());
        traineeDao.delete(traineeModel.getId());
    }

    @Override
    public TraineeModel get(UserCredentials credentials, long id) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        return traineeDao.get(id);
    }

    @Override
    public List<TraineeTrainingListItemDTO> getTrainingList(UserCredentials credentials, String dateFrom, String dateTo, String trainerUserName, String trainingType) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("trainee", credentials.getUserName());
        if (!trainerUserName.isBlank()) {
            parameters.put("trainer", trainerUserName);
        }
        if (!dateFrom.isBlank() && !dateTo.isBlank()) {
            parameters.put("startDate", DateUtils.parseDate(dateFrom));
            parameters.put("endDate", DateUtils.parseDate(dateTo));
        }
        if (!trainingType.isBlank()) {
            parameters.put("trainingType", TrainingTypeEnum.valueOf(trainingType).getId());
        }
        List<TrainingModel> trainingModelList = trainingDao.getTraineeTrainingListByParameters(parameters);
        List<TraineeTrainingListItemDTO> resultList = new ArrayList<>();
        for (TrainingModel trainingModel : trainingModelList) {
            TraineeTrainingListItemDTO traineeTrainingListItemDTO = DTOMapper.mapTrainingModelToTraineeTrainingItem(trainingModel);
            String trainerName = trainerDao.get(trainingModel.getTrainerId()).getUserName();
            traineeTrainingListItemDTO.setTrainerName(trainerName);
            resultList.add(traineeTrainingListItemDTO);
        }
        return resultList;
    }

    @Override
    public List<TrainerModel> getAssignedTrainerList(UserCredentials credentials) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        TraineeModel traineeModel = traineeDao.getByUserName(credentials.getUserName());
        return traineeDao.getAssignedTrainerList(traineeModel);
    }

    @Override
    public List<TrainerModel> getNotAssignedTrainerList(UserCredentials credentials) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        TraineeModel traineeModel = traineeDao.getByUserName(credentials.getUserName());
        return traineeDao.getNotAssignedTrainerList(traineeModel);
    }

    @Override
    public List<TrainerModel> updateTrainerList(UserCredentials credentials, List<String> newTrainerModelList) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        List<TrainerModel> newTrainerList = newTrainerModelList.stream().map(trainerDao::getByUserName).toList();
        TraineeModel traineeModel = traineeDao.getByUserName(credentials.getUserName());
        List<TrainerModel> existingTrainerList = traineeDao.getAssignedTrainerList(traineeModel);
        existingTrainerList.forEach(i -> traineeDao.deleteTrainer(traineeModel, i));
        newTrainerList.forEach(i -> traineeDao.intendTrainer(traineeModel, i));
        return traineeDao.getAssignedTrainerList(traineeModel);
    }

    private void setActiveStatus(UserCredentials credentials, boolean status) throws ValidationException, IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        TraineeModel trainee = traineeDao.getByUserName(credentials.getUserName());
        trainee.setIsActive(status);
        validator.validate(trainee);
        traineeDao.update(trainee);
    }
}
