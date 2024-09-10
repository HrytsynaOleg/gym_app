package com.gym.service.impl;

import com.gym.dao.ITrainingDao;
import com.gym.model.Training;
import com.gym.model.TrainingType;
import com.gym.service.ITrainingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Log4j2
public class TrainingService implements ITrainingService {
    @Autowired
    private ITrainingDao trainingDao;

    @Override
    public Training createTraining(long traineeId, long trainerId, String trainingName, String trainingTypeString,
                                   String trainingDateString, int duration) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate trainingDate = LocalDate.parse(trainingDateString, formatter);
        TrainingType trainingType = TrainingType.valueOf(trainingTypeString);
        Training training = Training.builder()
                .id(0)
                .trainerId(trainerId)
                .traineeId(traineeId)
                .trainingName(trainingName)
                .trainingType(trainingType)
                .trainingDate(trainingDate)
                .duration(duration)
                .build();
        Training newTraining = trainingDao.create(training);
        log.info("New training created");
        return newTraining;
    }

    @Override
    public Training getById(long id) {
        return trainingDao.get(id);
    }
}
