package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dao.ITraineeDao;
import com.gym.model.Trainee;
import com.gym.utils.StorageUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import com.gym.utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@Log4j2
class TraineeDaoTest {

    private final ITraineeDao dao;
    private Trainee serviceInputTrainee;

    TraineeDaoTest() {
        Map<String, Trainee> storage = StorageUtils.buildMapFromFile("C:/GYM/trainees.json",
                new TypeReference<>() {
                });
        this.dao = new TraineeDao(storage);
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trainee.json")) {
            this.serviceInputTrainee = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
        } catch (IOException ex) {
            log.error("Error reading source file");
        }
    }

    @Test
    void addTraineeTest() {
            Trainee newTrainee = dao.add(serviceInputTrainee);
            Trainee testNewTrainee = dao.getById(newTrainee.getId());

            assertNotNull(testNewTrainee);
            assertEquals(newTrainee.getId(), testNewTrainee.getId());
            assertEquals(serviceInputTrainee.getFirstName(), testNewTrainee.getFirstName());
            assertEquals(serviceInputTrainee.getLastName(), testNewTrainee.getLastName());
            assertEquals(serviceInputTrainee.getUserName(), testNewTrainee.getUserName());
            assertEquals(serviceInputTrainee.getPassword(), testNewTrainee.getPassword());
            assertTrue(serviceInputTrainee.getIsActive());
            assertEquals(serviceInputTrainee.getAddress(), testNewTrainee.getAddress());
            assertEquals(serviceInputTrainee.getDateOfBirth(), testNewTrainee.getDateOfBirth());
    }

    @Test
    void updateTraineeTest() {
        Trainee trainee = dao.getById(215);
        assertEquals("Serhiy", trainee.getFirstName());
        trainee.setFirstName("Petro");
        dao.update(trainee);
        Trainee updatedTrainee = dao.getById(215);
        assertEquals("Petro", updatedTrainee.getFirstName());
    }

    @Test
    void deleteTraineeTest() {
        Trainee trainee = dao.getById(216);
        assertEquals("Maxym", trainee.getFirstName());
        dao.delete(216);
        Trainee deletedTrainee = dao.getById(216);
        assertNull(deletedTrainee);
    }

    @Test
    void getByIdTest() {
        Trainee trainee = dao.getById(225);
        assertEquals(225, trainee.getId());
        assertEquals("Tom", trainee.getFirstName());
        assertEquals("Arraya", trainee.getLastName());
        assertEquals("Tom.Arraya", trainee.getUserName());
        assertEquals("123456", trainee.getPassword());
        assertTrue(trainee.getIsActive());
        assertEquals("123456", trainee.getPassword());
    }

    @Test
    void getByIdIfNotExistTest() {
        Trainee trainee = dao.getById(230);
        assertNull(trainee);
    }
}