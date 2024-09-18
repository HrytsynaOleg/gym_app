package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dao.ITrainerDao;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.utils.StorageUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import com.gym.utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@Log4j2
class TrainerModelDaoTest {

    private final ITrainerDao dao;
    private TrainerModel serviceInputTrainerModel;

    TrainerModelDaoTest() {
        Map<String, TrainerModel> storage = StorageUtils.buildMapFromFile("C:/GYM/trainers.json",
                new TypeReference<>() {
                });
        this.dao = new TrainerDao(storage);
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trainer.json")) {
            this.serviceInputTrainerModel = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
        } catch (IOException ex) {
            log.error("Error reading source file");
        }
    }

    @Test
    void createTrainerTest() {
            TrainerModel newTrainerModel = dao.create(serviceInputTrainerModel);
            TrainerModel testNewTrainerModel = dao.get(newTrainerModel.getId());

            assertNotNull(testNewTrainerModel);
            assertEquals(newTrainerModel.getId(), testNewTrainerModel.getId());
            assertEquals(serviceInputTrainerModel.getFirstName(), testNewTrainerModel.getFirstName());
            assertEquals(serviceInputTrainerModel.getLastName(), testNewTrainerModel.getLastName());
            assertEquals(serviceInputTrainerModel.getTrainingType(), testNewTrainerModel.getTrainingType());
    }

    @Test
    void updateTrainerTest() {
        TrainerModel trainerModel = dao.get(124);
        assertEquals(TrainingTypeEnum.YOGA, trainerModel.getTrainingType());
        trainerModel.setTrainingType(TrainingTypeEnum.STRETCHING);
        dao.update(trainerModel);
        TrainerModel updatedTrainerModel = dao.get(124);
        assertEquals(TrainingTypeEnum.STRETCHING, updatedTrainerModel.getTrainingType());
    }

    @Test
    void getTest() {
        TrainerModel trainerModel = dao.get(125);
        assertEquals(125, trainerModel.getId());
        assertEquals("Dmytro", trainerModel.getFirstName());
        assertEquals("Sirenko", trainerModel.getLastName());
        assertEquals("Dmytro.Sirenko", trainerModel.getUserName());
        assertEquals("123456", trainerModel.getPassword());
        assertEquals(TrainingTypeEnum.YOGA, trainerModel.getTrainingType());
        assertTrue(trainerModel.getIsActive());
    }

    @Test
    void getIfNotExistTest() {
        TrainerModel trainerModel = dao.get(180);
        assertNull(trainerModel);
    }
}