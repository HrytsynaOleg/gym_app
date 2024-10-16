package com.gym.service.impl;

import com.gym.dao.ITraineeDao;
import com.gym.dao.ITrainerDao;
import com.gym.dao.ITrainingDao;
import com.gym.dto.training.TrainingCreateDTO;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.service.ITrainingService;
import com.gym.service.IUserCredentialsService;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Log4j2
public class TrainingService implements ITrainingService {
    @Autowired
    private ITrainingDao trainingDao;
    @Autowired
    private ITrainerDao trainerDao;
    @Autowired
    private ITraineeDao traineeDao;
    @Autowired
    private IUserCredentialsService credentialsService;
    @Value("${date.format}")
    private String dateFormat;

    @Override
    public TrainingModel createTraining(TrainingCreateDTO trainingCreateDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDate trainingDate = LocalDate.parse(trainingCreateDTO.getTrainingDate(), formatter);
        TrainerModel trainer = trainerDao.getByUserName(trainingCreateDTO.getTrainerUserName());
        TraineeModel trainee = traineeDao.getByUserName(trainingCreateDTO.getTraineeUserName());
        TrainingTypeEnum trainingTypeEnum = trainer.getTrainingType();
        TrainingModel trainingModel = TrainingModel.builder()
                .id(0)
                .trainerId(trainer.getId())
                .traineeId(trainee.getId())
                .trainingName(trainingCreateDTO.getTrainingName())
                .trainingType(trainingTypeEnum)
                .trainingDate(trainingDate)
                .duration(Integer.parseInt(trainingCreateDTO.getTrainingDuration()))
                .build();
        TrainingModel newTrainingModel = trainingDao.create(trainingModel);
        log.info("New training created. Transaction Id {}", MDC.get("transactionId"));
        return newTrainingModel;
    }

    @Override
    public TrainingModel getById(long id) {
        return trainingDao.get(id);
    }
}
