package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dao.ITraineeDao;
import com.gym.model.TraineeModel;
import com.gym.utils.StorageUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import com.gym.utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@Log4j2
class TraineeModelDaoTest {

    private final ITraineeDao dao;
    private TraineeModel serviceInputTraineeModel;

    TraineeModelDaoTest() {
        Map<String, TraineeModel> storage = StorageUtils.buildMapFromFile("C:/GYM/trainees.json",
                new TypeReference<>() {
                });
        this.dao = new TraineeDao(storage);
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trainee.json")) {
            this.serviceInputTraineeModel = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
        } catch (IOException ex) {
            log.error("Error reading source file");
        }
    }

    @Test
    void addTraineeTest() {
            TraineeModel newTraineeModel = dao.add(serviceInputTraineeModel);
            TraineeModel testNewTraineeModel = dao.getById(newTraineeModel.getId());

            assertNotNull(testNewTraineeModel);
            assertEquals(newTraineeModel.getId(), testNewTraineeModel.getId());
            assertEquals(serviceInputTraineeModel.getFirstName(), testNewTraineeModel.getFirstName());
            assertEquals(serviceInputTraineeModel.getLastName(), testNewTraineeModel.getLastName());
            assertEquals(serviceInputTraineeModel.getUserName(), testNewTraineeModel.getUserName());
            assertEquals(serviceInputTraineeModel.getPassword(), testNewTraineeModel.getPassword());
            assertTrue(serviceInputTraineeModel.getIsActive());
            assertEquals(serviceInputTraineeModel.getAddress(), testNewTraineeModel.getAddress());
            assertEquals(serviceInputTraineeModel.getDateOfBirth(), testNewTraineeModel.getDateOfBirth());
    }

    @Test
    void updateTraineeTest() {
        TraineeModel traineeModel = dao.getById(215);
        assertEquals("Serhiy", traineeModel.getFirstName());
        traineeModel.setFirstName("Petro");
        dao.update(traineeModel);
        TraineeModel updatedTraineeModel = dao.getById(215);
        assertEquals("Petro", updatedTraineeModel.getFirstName());
    }

    @Test
    void deleteTraineeTest() {
        TraineeModel traineeModel = dao.getById(216);
        assertEquals("Maxym", traineeModel.getFirstName());
        dao.delete(216);
        TraineeModel deletedTraineeModel = dao.getById(216);
        assertNull(deletedTraineeModel);
    }

    @Test
    void getByIdTest() {
        TraineeModel traineeModel = dao.getById(225);
        assertEquals(225, traineeModel.getId());
        assertEquals("Tom", traineeModel.getFirstName());
        assertEquals("Arraya", traineeModel.getLastName());
        assertEquals("Tom.Arraya", traineeModel.getUserName());
        assertEquals("123456", traineeModel.getPassword());
        assertTrue(traineeModel.getIsActive());
        assertEquals("123456", traineeModel.getPassword());
    }

    @Test
    void getByIdIfNotExistTest() {
        TraineeModel traineeModel = dao.getById(230);
        assertNull(traineeModel);
    }
}