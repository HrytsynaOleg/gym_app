package com.gym.service.impl;

import com.gym.dao.impl.TrainingDao;
import com.gym.model.Training;
import com.gym.model.TrainingType;
import com.gym.service.ITrainingService;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainingServiceTest {
    private final ITrainingService trainingService;
    private final TrainingDao mockTrainingDao;
    private final long trainerId = 256;
    private final long traineeId = 125;
    private final String trainingDateInString = "2024-09-18";
    private final LocalDate trainingDate = LocalDate.of(2024, 9, 18);
    private final String trainingName = "My first training";
    private final String trainingTypeString = "FITNESS";
    private final TrainingType trainingType = TrainingType.FITNESS;
    private int duration = 60;

    public TrainingServiceTest() {
        this.trainingService = new TrainingService();
        this.mockTrainingDao = Mockito.mock(TrainingDao.class);

        Field trainingDaoField = ReflectionUtils
                .findFields(TrainingService.class, f -> f.getName().equals("trainingDao"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);
        trainingDaoField.setAccessible(true);
        try {
            trainingDaoField.set(trainingService, mockTrainingDao);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createTraineeTest() {
        Training serviceCreatedTraining = Training.builder()
                .id(0)
                .traineeId(traineeId)
                .trainerId(trainerId)
                .trainingName(trainingName)
                .trainingType(trainingType)
                .trainingDate(trainingDate)
                .duration(duration)
                .build();
        Training daoResponseTraining = Training.builder()
                .id(256)
                .traineeId(traineeId)
                .trainerId(trainerId)
                .trainingName(trainingName)
                .trainingType(trainingType)
                .trainingDate(trainingDate)
                .duration(duration)
                .build();

        Mockito.when(mockTrainingDao.create(serviceCreatedTraining)).thenReturn(daoResponseTraining);

        Training newTraining = trainingService.createTraining(traineeId, trainerId, trainingName,
                trainingTypeString, trainingDateInString, duration);

        assertEquals(256, newTraining.getId());
        assertEquals(traineeId, newTraining.getTraineeId());
        assertEquals(trainerId, newTraining.getTrainerId());
        assertEquals(trainingName, newTraining.getTrainingName());
        assertEquals(trainingType, newTraining.getTrainingType());
        assertEquals(trainingDate, newTraining.getTrainingDate());
        assertEquals(duration, newTraining.getDuration());
    }
}