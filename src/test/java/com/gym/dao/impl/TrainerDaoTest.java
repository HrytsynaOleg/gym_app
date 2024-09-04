package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dao.ITrainerDao;
import com.gym.model.Trainer;
import com.gym.model.TrainingType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TrainerDaoTest {
    @Autowired
    ITrainerDao dao;

    @Test
    void addTrainerTest() {
        Trainer testTrainer;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trainer.json")) {
            testTrainer = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
            Trainer newTrainer = dao.add(testTrainer);
            Trainer trainer = dao.getById(newTrainer.getId());
            assertEquals(newTrainer.getId(), trainer.getId());
            assertEquals(newTrainer.getFirstName(), trainer.getFirstName());
            assertEquals(newTrainer.getLastName(), trainer.getLastName());
            assertEquals(newTrainer.getTrainingType(), trainer.getTrainingType());
        } catch (IOException ex) {
            System.err.println(ex.getCause());
        }
    }

    @Test
    void updateTrainerTest() {
        Trainer trainer = dao.getById(124);
        assertEquals(TrainingType.YOGA, trainer.getTrainingType());
        trainer.setTrainingType(TrainingType.STRETCHING);
        dao.update(trainer);
        Trainer updatedTrainer = dao.getById(124);
        assertEquals(TrainingType.STRETCHING, updatedTrainer.getTrainingType());
    }

    @Test
    void getByIdTest() {
        Trainer trainer = dao.getById(125);
        assertEquals(125, trainer.getId());
        assertEquals("Dmytro", trainer.getFirstName());
        assertEquals("Sirenko", trainer.getLastName());
        assertEquals("Dmytro.Sirenko", trainer.getUserName());
        assertEquals("123456", trainer.getPassword());
        assertEquals(TrainingType.YOGA, trainer.getTrainingType());
        assertTrue(trainer.getIsActive());
    }

    @Test
    void getByIdIfNotExistTest() {
        Trainer trainer = dao.getById(180);
        assertNull(trainer);
    }
}