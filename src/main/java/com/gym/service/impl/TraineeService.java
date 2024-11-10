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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.gym.utils.StringUtils;

import jakarta.validation.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private final PasswordEncoder passwordEncoder;

    public TraineeService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public TraineeModel createTrainee(String firstName, String lastName, String address, String dateOfBirth) {
        long usersCount = userDao.getUserCount(firstName, lastName);
        String userName = StorageUtils.generateUserName(firstName, lastName, usersCount);
        String generatedPassword = StringUtils.generateRandomString(passwordLength);
        String password = "{bcrypt}" + passwordEncoder.encode(generatedPassword);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDate localDate = LocalDate.parse(dateOfBirth, formatter);
        TraineeModel traineeModel = TraineeModel.builder()
                .id(0)
                .firstName(firstName)
                .lastName(lastName)
                .userName(userName)
                .password(password)
                .isActive(true).address(address)
                .dateOfBirth(localDate).build();
        validator.validate(traineeModel);
        TraineeModel newTraineeModel = traineeDao.create(traineeModel);
        newTraineeModel.setPassword(generatedPassword);
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
    public TraineeModel getTraineeProfile(String username) throws NoSuchElementException {
        return traineeDao.getByUserName(username);
    }

    @Override
    public void activate(String username) throws ValidationException, NoSuchElementException {
        setActiveStatus(username, true);
        log.info("User activated. Transaction Id {}", MDC.get("transactionId"));
    }

    @Override
    public void deactivate(String username) throws NoSuchElementException {
        setActiveStatus(username, false);
        log.info("User deactivated. Transaction Id {}", MDC.get("transactionId"));
    }

    @Override
    public TraineeModel update(String username, TraineeModel traineeModel) throws NoSuchElementException {
        traineeDao.update(traineeModel);
        TraineeModel updatedTraineeModel = traineeDao.getByUserName(username);
        log.info("Trainee profile updated. Transaction Id {}", MDC.get("transactionId"));
        return updatedTraineeModel;
    }

    @Override
    public void delete(String username) throws NoSuchElementException {
        TraineeModel traineeModel = traineeDao.getByUserName(username);
        traineeDao.delete(traineeModel.getId());
    }

    @Override
    public TraineeModel get(UserCredentials credentials, long id) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        return traineeDao.get(id);
    }

    @Override
    public List<TraineeTrainingListItemDTO> getTrainingList(String username, String dateFrom, String dateTo,
                                                            String trainerUserName, String trainingType)
            throws NoSuchElementException {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("trainee", username);
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
    public List<TrainerModel> getAssignedTrainerList(String username) throws NoSuchElementException {
        TraineeModel traineeModel = traineeDao.getByUserName(username);
        return traineeDao.getAssignedTrainerList(traineeModel);
    }

    @Override
    public List<TrainerModel> getNotAssignedTrainerList(String username) throws NoSuchElementException {
        TraineeModel traineeModel = traineeDao.getByUserName(username);
        return traineeDao.getNotAssignedTrainerList(traineeModel);
    }

    @Override
    public List<TrainerModel> updateTrainerList(String username, List<String> newTrainerModelList)
            throws NoSuchElementException {
        List<TrainerModel> newTrainerList = newTrainerModelList.stream().map(trainerDao::getByUserName).toList();
        TraineeModel traineeModel = traineeDao.getByUserName(username);
        List<TrainerModel> existingTrainerList = traineeDao.getAssignedTrainerList(traineeModel);
        existingTrainerList.forEach(i -> traineeDao.deleteTrainer(traineeModel, i));
        newTrainerList.forEach(i -> traineeDao.intendTrainer(traineeModel, i));
        return traineeDao.getAssignedTrainerList(traineeModel);
    }

    private void setActiveStatus(String username, boolean status) throws ValidationException, NoSuchElementException {
        TraineeModel trainee = traineeDao.getByUserName(username);
        trainee.setIsActive(status);
        validator.validate(trainee);
        traineeDao.update(trainee);
    }
}
