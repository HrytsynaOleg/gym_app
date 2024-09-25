package com.gym.service.impl;

import com.gym.dao.ITrainingDao;
import com.gym.model.TrainingModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.service.ITrainingService;
import com.gym.service.IUserCredentialsService;
import lombok.extern.log4j.Log4j2;
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
    private IUserCredentialsService credentialsService;
    @Value("${date.format}")
    private String dateFormat;

    @Override
    public TrainingModel createTraining(long traineeId, long trainerId, String trainingName, String trainingTypeString,
                                        String trainingDateString, int duration) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDate trainingDate = LocalDate.parse(trainingDateString, formatter);
        TrainingTypeEnum trainingTypeEnum = TrainingTypeEnum.valueOf(trainingTypeString);
        TrainingModel trainingModel = TrainingModel.builder()
                .id(0)
                .trainerId(trainerId)
                .traineeId(traineeId)
                .trainingName(trainingName)
                .trainingType(trainingTypeEnum)
                .trainingDate(trainingDate)
                .duration(duration)
                .build();
        TrainingModel newTrainingModel = trainingDao.create(trainingModel);
        log.info("New training created");
        return newTrainingModel;
    }

    @Override
    public TrainingModel getById(long id) {
        return trainingDao.get(id);
    }
}
