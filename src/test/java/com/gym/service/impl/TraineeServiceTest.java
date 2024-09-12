package com.gym.service.impl;

import com.gym.dao.impl.TraineeDao;
import com.gym.dao.impl.TrainerDao;
import com.gym.model.Trainee;
import com.gym.service.ITraineeService;
import com.gym.utils.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TraineeServiceTest {
    private final ITraineeService traineeService;
    private final TraineeDao mockTraineeDao;
    private final TrainerDao mockTrainerDao;
    private final String address = "Los Angeles";
    private final String dateOfBirthInString = "1999-02-15";
    private final LocalDate dateOfBirth;
    private final String firstName = "Valeriy";
    private final String lastName = "Tokar";
    private final String userName = "Valeriy.Tokar";
    private final String password = "1234567890";
    private final Integer passwordLength = 10;
    private MockedStatic<StringUtils> mockStringUtil;

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
        Field passwordLengthField = ReflectionUtils
                .findFields(TraineeService.class, f -> f.getName().equals("passwordLength"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);
        passwordLengthField.setAccessible(true);
        try {
            trainerDaoField.set(traineeService, mockTrainerDao);
            traineeDaoField.set(traineeService, mockTraineeDao);
            passwordLengthField.set(traineeService, passwordLength);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateOfBirth = LocalDate.parse(dateOfBirthInString, formatter);
    }

    @BeforeEach
    public void registerStaticMock() {
        mockStringUtil = Mockito.mockStatic(StringUtils.class);
        mockStringUtil.when(() -> StringUtils.generateRandomString(passwordLength))
                .thenReturn("1234567890");
    }

    @AfterEach
    public void unregisterStaticMock() {
        mockStringUtil.close();
    }

    @Test
    void createTraineeTest() {
        Trainee serviceCreatedTrainee = Trainee.builder()
                .id(0)
                .firstName(firstName)
                .lastName(lastName)
                .userName(userName)
                .password(password)
                .isActive(true)
                .address(address)
                .dateOfBirth(dateOfBirth)
                .build();
        Trainee daoResponseTrainee = Trainee.builder()
                .id(18)
                .firstName(firstName)
                .lastName(lastName)
                .userName(userName)
                .password(password)
                .isActive(true)
                .address(address)
                .dateOfBirth(dateOfBirth)
                .build();

        Mockito.when(mockTraineeDao.add(serviceCreatedTrainee)).thenReturn(daoResponseTrainee);
        Mockito.when(mockTraineeDao.getUserCountByUserName(firstName, lastName)).thenReturn(0L);
        Mockito.when(mockTrainerDao.getUserCountByUserName(firstName, lastName)).thenReturn(0L);

        Trainee newTrainee = traineeService.createTrainee(firstName, lastName, address, dateOfBirthInString);

        assertEquals(18, newTrainee.getId());
        assertEquals(firstName, newTrainee.getFirstName());
        assertEquals(lastName, newTrainee.getLastName());
        assertEquals(userName, newTrainee.getUserName());
        assertEquals(passwordLength, newTrainee.getPassword().length());
        assertEquals(address, newTrainee.getAddress());
        assertEquals(dateOfBirth, newTrainee.getDateOfBirth());
    }

    @Test
    void createTraineeIfUserExistTest() {
        String expectedUserName = userName + 1;
        Trainee serviceCreatedTrainee = Trainee.builder()
                .id(0)
                .firstName(firstName)
                .lastName(lastName)
                .userName(expectedUserName)
                .password(password)
                .isActive(true)
                .address(address)
                .dateOfBirth(dateOfBirth)
                .build();
        Trainee daoResponseTrainee = Trainee.builder()
                .id(18)
                .firstName(firstName)
                .lastName(lastName)
                .userName(expectedUserName)
                .password(password)
                .isActive(true)
                .address(address)
                .dateOfBirth(dateOfBirth)
                .build();

        Mockito.when(mockTraineeDao.add(serviceCreatedTrainee)).thenReturn(daoResponseTrainee);
        Mockito.when(mockTraineeDao.getUserCountByUserName(firstName, lastName)).thenReturn(1L);
        Mockito.when(mockTrainerDao.getUserCountByUserName(firstName, lastName)).thenReturn(0L);

        Trainee newTrainee = traineeService.createTrainee(firstName, lastName, address, dateOfBirthInString);

        assertEquals(18, newTrainee.getId());
        assertEquals(firstName, newTrainee.getFirstName());
        assertEquals(lastName, newTrainee.getLastName());
        assertEquals(expectedUserName, newTrainee.getUserName());
        assertEquals(passwordLength, newTrainee.getPassword().length());
        assertEquals(address, newTrainee.getAddress());
        assertEquals(dateOfBirth, newTrainee.getDateOfBirth());
    }
}