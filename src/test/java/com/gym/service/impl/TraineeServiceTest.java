package com.gym.service.impl;

import com.gym.model.Trainee;
import com.gym.service.ITraineeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TraineeServiceTest {
    @Autowired
    private ITraineeService traineeService;
    private final String address = "Los Angeles";
    private final String dateOfBirth = "1999/02/15";

    @Test
    void createTraineeTest() {
        String firstName = "Valeriy";
        String lastName = "Tokar";
        Trainee newTrainee = traineeService.createTrainee(firstName, lastName, address, dateOfBirth);
        Trainee trainee = traineeService.getById(newTrainee.getId());
        assertEquals(firstName, trainee.getFirstName());
        assertEquals(lastName, trainee.getLastName());
        assertEquals("Valeriy.Tokar", trainee.getUserName());
        assertEquals(10, trainee.getPassword().length());
        assertEquals(address, trainee.getAddress());
        assertEquals(dateOfBirth, trainee.getDateOfBirth());
    }

    @Test
    void createTraineeIfUserExistTest() {
        String firstName = "Pavlo";
        String lastName = "Yakimets";
        Trainee newTrainee = traineeService.createTrainee(firstName, lastName, address, dateOfBirth);
        Trainee trainee = traineeService.getById(newTrainee.getId());
        assertEquals(firstName, trainee.getFirstName());
        assertEquals(lastName, trainee.getLastName());
        assertEquals("Pavlo.Yakimets1", trainee.getUserName());
    }

    @Test
    void createTraineeIfUserExistInTrainersTest() {
        String firstName = "Dmytro";
        String lastName = "Sirenko";
        Trainee newTrainee = traineeService.createTrainee(firstName, lastName, address, dateOfBirth);
        Trainee trainee = traineeService.getById(newTrainee.getId());
        assertEquals(firstName, trainee.getFirstName());
        assertEquals(lastName, trainee.getLastName());
        assertEquals("Dmytro.Sirenko1", trainee.getUserName());
    }

    @Test
    void updateTraineeTest() {
        Trainee trainee = traineeService.getById(216);
        assertEquals("Shvets", trainee.getLastName());
        trainee.setLastName("Melnyk");
        traineeService.update(trainee);
        Trainee updatedTrainee = traineeService.getById(216);
        assertEquals("Melnyk", updatedTrainee.getLastName());
    }

    @Test
    void getTraineeByIdTest() {
        Trainee trainee = traineeService.getById(225);
        assertEquals(225, trainee.getId());
        assertEquals("Tom", trainee.getFirstName());
        assertEquals("Arraya", trainee.getLastName());
        assertEquals("Tom.Arraya", trainee.getUserName());
        assertEquals("123456", trainee.getPassword());
        assertEquals("Kharkiv, Nauki 45", trainee.getAddress());
        assertEquals("2001/05/24", trainee.getDateOfBirth());
        assertTrue(trainee.getIsActive());
    }

    @Test
    void getTraineeByIdIfNotExistTest() {
        Trainee trainee = traineeService.getById(290);
        assertNull(trainee);
    }
}