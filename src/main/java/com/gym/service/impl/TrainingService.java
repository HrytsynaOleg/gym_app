package com.gym.service.impl;

import com.gym.dao.ITrainingDao;
import com.gym.model.Training;
import com.gym.model.TrainingType;
import com.gym.service.ITrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TrainingService implements ITrainingService {
    @Autowired
    private ITrainingDao trainingDao;

    @Override
    public Training createTraining(long traineeId, long trainerId, String trainingName, TrainingType trainingType,
                                   LocalDate trainingDate, int duration) {
        Training training = new Training();
        training.setId(0);
        training.setTraineeId(traineeId);
        training.setTrainerId(trainerId);
        training.setTrainingName(trainingName);
        training.setTrainingType(trainingType);
        training.setTrainingDate(trainingDate);
        training.setDuration(duration);
        Training newTraining = trainingDao.add(training);
        return newTraining;
    }

    @Override
    public Training getById(long id) {
        return trainingDao.getById(id);
    }
}
