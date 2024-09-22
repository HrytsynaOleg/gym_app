package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dao.ITrainingDao;
import com.gym.model.TrainingModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.utils.JsonUtils;
import com.gym.utils.StorageUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class TrainingModelDaoTest {

    private final ITrainingDao dao;
    private TrainingModel serviceInputTrainingModel;

    TrainingModelDaoTest() {
        Map<String, TrainingModel> storage = StorageUtils.buildMapFromFile("C:/GYM/training.json",
                new TypeReference<>() {
                });
        this.dao = new TrainingDao(storage);
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("training.json")) {
            this.serviceInputTrainingModel = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
        } catch (IOException ex) {
            log.error("Error reading source file");
        }
    }

    @Test
    void createTrainingTest() {

        TrainingModel newTrainingModel = dao.create(serviceInputTrainingModel);
        TrainingModel testNewTrainingModel = dao.get(newTrainingModel.getId());

        assertNotNull(testNewTrainingModel);
        assertEquals(newTrainingModel.getId(), testNewTrainingModel.getId());
        assertEquals(serviceInputTrainingModel.getTrainerId(), testNewTrainingModel.getTrainerId());
        assertEquals(serviceInputTrainingModel.getTraineeId(), testNewTrainingModel.getTraineeId());
        assertEquals(serviceInputTrainingModel.getTrainingName(), testNewTrainingModel.getTrainingName());
        assertEquals(serviceInputTrainingModel.getTrainingType(), testNewTrainingModel.getTrainingType());
        assertEquals(serviceInputTrainingModel.getTrainingDate(), testNewTrainingModel.getTrainingDate());
        assertEquals(serviceInputTrainingModel.getDuration(), testNewTrainingModel.getDuration());
    }

    @Test
    void getTest() {
        TrainingModel trainingModel = dao.get(365);
        assertNotNull(trainingModel);
        assertEquals(365, trainingModel.getId());
        assertEquals(124, trainingModel.getTrainerId());
        assertEquals(215, trainingModel.getTraineeId());
        assertEquals("first training", trainingModel.getTrainingName());
        assertEquals(TrainingTypeEnum.YOGA, trainingModel.getTrainingType());
        assertEquals(LocalDate.of(2024, 8, 16), trainingModel.getTrainingDate());
        assertEquals(30, trainingModel.getDuration());
    }

    @Test
    void getIfNotExistTest() {
        TrainingModel trainingModel = dao.get(180);
        assertNull(trainingModel);
    }
}