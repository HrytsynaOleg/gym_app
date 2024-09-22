package com.gym.service.impl;

import com.gym.dao.impl.TrainingDao;
import com.gym.model.TrainingModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.service.ITrainingService;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainingModelServiceTest {
    private final ITrainingService trainingService;
    private final TrainingDao mockTrainingDao;
    private final long trainerId = 256;
    private final long traineeId = 125;
    private final String trainingDateInString = "2024-09-18";
    private final LocalDate trainingDate = LocalDate.of(2024, 9, 18);
    private final String trainingName = "My first training";
    private final String trainingTypeString = "FITNESS";
    private final TrainingTypeEnum trainingTypeEnum = TrainingTypeEnum.FITNESS;
    private int duration = 60;

    public TrainingModelServiceTest() {
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
        TrainingModel serviceCreatedTrainingModel = TrainingModel.builder()
                .id(0)
                .traineeId(traineeId)
                .trainerId(trainerId)
                .trainingName(trainingName)
                .trainingType(trainingTypeEnum)
                .trainingDate(trainingDate)
                .duration(duration)
                .build();
        TrainingModel daoResponseTrainingModel = TrainingModel.builder()
                .id(256)
                .traineeId(traineeId)
                .trainerId(trainerId)
                .trainingName(trainingName)
                .trainingType(trainingTypeEnum)
                .trainingDate(trainingDate)
                .duration(duration)
                .build();

        Mockito.when(mockTrainingDao.create(serviceCreatedTrainingModel)).thenReturn(daoResponseTrainingModel);

        TrainingModel newTrainingModel = trainingService.createTraining(traineeId, trainerId, trainingName,
                trainingTypeString, trainingDateInString, duration);

        assertEquals(256, newTrainingModel.getId());
        assertEquals(traineeId, newTrainingModel.getTraineeId());
        assertEquals(trainerId, newTrainingModel.getTrainerId());
        assertEquals(trainingName, newTrainingModel.getTrainingName());
        assertEquals(trainingTypeEnum, newTrainingModel.getTrainingType());
        assertEquals(trainingDate, newTrainingModel.getTrainingDate());
        assertEquals(duration, newTrainingModel.getDuration());
    }
}