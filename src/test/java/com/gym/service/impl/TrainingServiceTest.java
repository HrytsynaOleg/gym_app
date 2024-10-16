package com.gym.service.impl;

import com.gym.dto.training.TrainingCreateDTO;
import com.gym.model.TrainingModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.service.ITrainingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TrainingServiceTest {
    private final ITrainingService trainingService;
    private final long trainerId = 117;
    private final long traineeId = 265;
    private final String trainingDateInString = "2024-09-18";
    private final LocalDate trainingDate = LocalDate.of(2024, 9, 18);
    private final String trainingName = "My first training";
    private final TrainingTypeEnum trainingTypeEnum = TrainingTypeEnum.FITNESS;
    private final int duration = 60;

    @Autowired
    public TrainingServiceTest(ITrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @Test
    void createTrainingTest() {
        TrainingCreateDTO trainerCreateDTO = TrainingCreateDTO.builder()
                .trainerUserName("Tom.Arraya")
                .traineeUserName("James.Hetfield")
                .trainingName(trainingName)
                .trainingDate(trainingDateInString)
                .trainingDuration(String.valueOf(duration))
                .build();
        TrainingModel responseTrainingModel = trainingService.createTraining(trainerCreateDTO);
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