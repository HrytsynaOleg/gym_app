package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dao.ITrainingDao;
import com.gym.model.Training;
import com.gym.model.TrainingType;
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
class TrainingDaoTest {

    private final ITrainingDao dao;
    private Training serviceInputTraining;

    TrainingDaoTest() {
        Map<String, Training> storage = StorageUtils.buildMapFromFile("C:/GYM/training.json",
                new TypeReference<>() {
                });
        this.dao = new TrainingDao(storage);
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("training.json")) {
            this.serviceInputTraining = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
        } catch (IOException ex) {
            log.error("Error reading source file");
        }
    }

    @Test
    void createTrainingTest() {

        Training newTraining = dao.create(serviceInputTraining);
        Training testNewTraining = dao.get(newTraining.getId());

        assertNotNull(testNewTraining);
        assertEquals(newTraining.getId(), testNewTraining.getId());
        assertEquals(serviceInputTraining.getTrainerId(), testNewTraining.getTrainerId());
        assertEquals(serviceInputTraining.getTraineeId(), testNewTraining.getTraineeId());
        assertEquals(serviceInputTraining.getTrainingName(), testNewTraining.getTrainingName());
        assertEquals(serviceInputTraining.getTrainingType(), testNewTraining.getTrainingType());
        assertEquals(serviceInputTraining.getTrainingDate(), testNewTraining.getTrainingDate());
        assertEquals(serviceInputTraining.getDuration(), testNewTraining.getDuration());
    }

    @Test
    void getTest() {
        Training training = dao.get(365);
        assertNotNull(training);
        assertEquals(365, training.getId());
        assertEquals(124, training.getTrainerId());
        assertEquals(215, training.getTraineeId());
        assertEquals("first training", training.getTrainingName());
        assertEquals(TrainingType.YOGA, training.getTrainingType());
        assertEquals(LocalDate.of(2024, 8, 16), training.getTrainingDate());
        assertEquals(30, training.getDuration());
    }

    @Test
    void getIfNotExistTest() {
        Training training = dao.get(180);
        assertNull(training);
    }
}