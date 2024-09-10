package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dao.ITrainerDao;
import com.gym.model.Trainer;
import com.gym.model.TrainingType;
import com.gym.utils.StorageUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import com.gym.utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@Log4j2
class TrainerDaoTest {

    private final ITrainerDao dao;
    private Trainer serviceInputTrainer;

    TrainerDaoTest() {
        Map<String, Trainer> storage = StorageUtils.buildMapFromFile("C:/GYM/trainers.json",
                new TypeReference<>() {
                });
        this.dao = new TrainerDao(storage);
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trainer.json")) {
            this.serviceInputTrainer = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
        } catch (IOException ex) {
            log.error("Error reading source file");
        }
    }

    @Test
    void createTrainerTest() {
            Trainer newTrainer = dao.create(serviceInputTrainer);
            Trainer testNewTrainer = dao.get(newTrainer.getId());

            assertNotNull(testNewTrainer);
            assertEquals(newTrainer.getId(), testNewTrainer.getId());
            assertEquals(serviceInputTrainer.getFirstName(), testNewTrainer.getFirstName());
            assertEquals(serviceInputTrainer.getLastName(), testNewTrainer.getLastName());
            assertEquals(serviceInputTrainer.getTrainingType(), testNewTrainer.getTrainingType());
    }

    @Test
    void updateTrainerTest() {
        Trainer trainer = dao.get(124);
        assertEquals(TrainingType.YOGA, trainer.getTrainingType());
        trainer.setTrainingType(TrainingType.STRETCHING);
        dao.update(trainer);
        Trainer updatedTrainer = dao.get(124);
        assertEquals(TrainingType.STRETCHING, updatedTrainer.getTrainingType());
    }

    @Test
    void getTest() {
        Trainer trainer = dao.get(125);
        assertEquals(125, trainer.getId());
        assertEquals("Dmytro", trainer.getFirstName());
        assertEquals("Sirenko", trainer.getLastName());
        assertEquals("Dmytro.Sirenko", trainer.getUserName());
        assertEquals("123456", trainer.getPassword());
        assertEquals(TrainingType.YOGA, trainer.getTrainingType());
        assertTrue(trainer.getIsActive());
    }

    @Test
    void getIfNotExistTest() {
        Trainer trainer = dao.get(180);
        assertNull(trainer);
    }
}