package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.config.StorageConfig;
import com.gym.dao.ITraineeDao;
import com.gym.dao.ITrainingDao;
import com.gym.model.TraineeModel;
import com.gym.utils.StorageUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.gym.utils.JsonUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@Log4j2
class TraineeDaoTest {
    private static ApplicationContext applicationContext;
    private final ITraineeDao dao;
    private TraineeModel serviceInputTraineeModel;

    TraineeDaoTest() {
        this.dao = (ITraineeDao) applicationContext.getBean("trainingDao");
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trainee.json")) {
            this.serviceInputTraineeModel = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
        } catch (IOException ex) {
            log.error("Error reading source file");
        }
    }

    @BeforeAll
    public static void init() {
        applicationContext = new AnnotationConfigApplicationContext(StorageConfig.class);
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