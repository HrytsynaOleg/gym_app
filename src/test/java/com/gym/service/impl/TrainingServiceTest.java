package com.gym.service.impl;

import com.gym.config.StorageConfig;
import com.gym.model.TrainingModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.service.ITrainingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainingServiceTest {
    private final ITrainingService trainingService;
    private final long trainerId = 117;
    private final long traineeId = 265;
    private final String trainingDateInString = "2024-09-18";
    private final LocalDate trainingDate = LocalDate.of(2024, 9, 18);
    private final String trainingName = "My first training";
    private final String trainingTypeString = "FITNESS";
    private final TrainingTypeEnum trainingTypeEnum = TrainingTypeEnum.FITNESS;
    private final int duration = 60;
    private static ApplicationContext applicationContext;

    public TrainingServiceTest() {
        this.trainingService = (ITrainingService) applicationContext.getBean("trainingService");
    }

    @BeforeAll
    public static void init() {
        applicationContext = new AnnotationConfigApplicationContext(StorageConfig.class);
    }

    @Test
    void createTrainingTest() {

        TrainingModel responseTrainingModel = trainingService.createTraining(traineeId, trainerId, trainingName,
                trainingTypeString, trainingDateInString, duration);
        TrainingModel newTrainingModel = trainingService.getById(responseTrainingModel.getId());

        assertEquals(responseTrainingModel.getId(), newTrainingModel.getId());
        assertEquals(traineeId, newTrainingModel.getTraineeId());
        assertEquals(trainerId, newTrainingModel.getTrainerId());
        assertEquals(trainingName, newTrainingModel.getTrainingName());
        assertEquals(trainingTypeEnum, newTrainingModel.getTrainingType());
        assertEquals(trainingDate, newTrainingModel.getTrainingDate());
        assertEquals(duration, newTrainingModel.getDuration());
    }
}