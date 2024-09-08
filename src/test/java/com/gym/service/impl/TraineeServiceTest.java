package com.gym.service.impl;

import com.gym.dao.impl.TraineeDao;
import com.gym.dao.impl.TrainerDao;
import com.gym.model.Trainee;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class TraineeServiceTest {
    private final TraineeService traineeService;
    private final TraineeDao mockTraineeDao;
    private final TrainerDao mockTrainerDao;
    private final String address = "Los Angeles";
    private final String dateOfBirth = "1999-02-15";

    public TraineeServiceTest() {
        this.traineeService = new TraineeService();
        this.mockTraineeDao = Mockito.mock(TraineeDao.class);
        this.mockTrainerDao = Mockito.mock(TrainerDao.class);
        Field trainerDaoField = ReflectionUtils
                .findFields(TraineeService.class, f -> f.getName().equals("trainerDao"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);
        trainerDaoField.setAccessible(true);
        Field traineeDaoField = ReflectionUtils
                .findFields(TraineeService.class, f -> f.getName().equals("traineeDao"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);
        traineeDaoField.setAccessible(true);
        try {
            trainerDaoField.set(traineeService, mockTrainerDao);
            traineeDaoField.set(traineeService, mockTraineeDao);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createTraineeTest() {
        String firstName = "Valeriy";
        String lastName = "Tokar";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDateOfBirth = LocalDate.parse(dateOfBirth, formatter);
        Trainee mockTrainee = new Trainee();
        mockTrainee.setId(18);
        mockTrainee.setFirstName(firstName);
        mockTrainee.setLastName(lastName);
        mockTrainee.setUserName("Valeriy.Tokar");
        mockTrainee.setPassword("1234567890");
        mockTrainee.setIsActive(true);
        mockTrainee.setAddress(address);
        mockTrainee.setDateOfBirth(localDateOfBirth);

        Mockito.when(mockTraineeDao.add(any())).thenReturn(mockTrainee);
        Mockito.when(mockTraineeDao.getListByUserName("Valeriy.Tokar")).thenReturn(List.of());
        Mockito.when(mockTrainerDao.getListByUserName("Valeriy.Tokar")).thenReturn(List.of());
        Trainee newTrainee = traineeService.createTrainee(firstName, lastName, address, dateOfBirth);

        assertEquals(firstName, newTrainee.getFirstName());
        assertEquals(lastName, newTrainee.getLastName());
        assertEquals("Valeriy.Tokar", newTrainee.getUserName());
        assertEquals(10, newTrainee.getPassword().length());
        assertEquals(address, newTrainee.getAddress());
        assertEquals(localDateOfBirth, newTrainee.getDateOfBirth());
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