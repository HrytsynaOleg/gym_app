package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dao.ITraineeDao;
import com.gym.model.Trainee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class TraineeDaoTest {
    @Autowired
    ITraineeDao dao;

    @Test
    void addTraineeTest() {
        Trainee testTrainee;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trainee.json")) {
            testTrainee = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
            Trainee newTrainee = dao.add(testTrainee);
            Trainee trainee = dao.getById(newTrainee.getId());
            assertEquals(newTrainee.getId(), trainee.getId());
            assertEquals(newTrainee.getFirstName(), trainee.getFirstName());
            assertEquals(newTrainee.getLastName(), trainee.getLastName());
            assertEquals(newTrainee.getUserName(), trainee.getUserName());
            assertEquals(newTrainee.getPassword(), trainee.getPassword());
            assertTrue(trainee.getIsActive());
            assertEquals(newTrainee.getAddress(), trainee.getAddress());
            assertEquals(newTrainee.getDateOfBirth(), trainee.getDateOfBirth());
        } catch (IOException ex) {
            System.err.println(ex.getCause());
        }
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
        dao.delete(trainee);
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