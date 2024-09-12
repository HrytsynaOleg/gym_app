package com.gym.service.impl;

import com.gym.dao.impl.TraineeDao;
import com.gym.dao.impl.TrainerDao;
import com.gym.model.Trainer;
import com.gym.model.TrainingType;
import com.gym.service.ITrainerService;
import com.gym.utils.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class TrainerServiceTest {
    private final ITrainerService trainerService;
    private final TraineeDao mockTraineeDao;
    private final TrainerDao mockTrainerDao;
    private final String firstName = "Kerry";
    private final String lastName = "King";
    private final String userName = "Kerry.King";
    private final String password = "1234567890";
    private final TrainingType trainingType = TrainingType.YOGA;
    private final String trainingTypeString = "YOGA";
    private MockedStatic<StringUtils> mockStringUtil;
    private final Integer passwordLength = 10;

    TrainerServiceTest() {
        this.trainerService = new TrainerService();
        this.mockTraineeDao = Mockito.mock(TraineeDao.class);
        this.mockTrainerDao = Mockito.mock(TrainerDao.class);
        Field trainerDaoField = ReflectionUtils
                .findFields(TrainerService.class, f -> f.getName().equals("trainerDao"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);
        trainerDaoField.setAccessible(true);
        Field traineeDaoField = ReflectionUtils
                .findFields(TrainerService.class, f -> f.getName().equals("traineeDao"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);
        traineeDaoField.setAccessible(true);
        Field passwordLengthField = ReflectionUtils
                .findFields(TrainerService.class, f -> f.getName().equals("passwordLength"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);
        passwordLengthField.setAccessible(true);
        try {
            trainerDaoField.set(trainerService, mockTrainerDao);
            traineeDaoField.set(trainerService, mockTraineeDao);
            passwordLengthField.set(trainerService, passwordLength);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

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
    void createTrainerTest() {
        Trainer serviceCreatedTrainer = Trainer.builder()
                .id(0)
                .firstName(firstName)
                .lastName(lastName)
                .userName(userName)
                .password(password)
                .isActive(true)
                .trainingType(trainingType)
                .build();
        Trainer daoResponseTrainer = Trainer.builder()
                .id(25)
                .firstName(firstName)
                .lastName(lastName)
                .userName(userName)
                .password(password)
                .isActive(true)
                .trainingType(trainingType)
                .build();

        Mockito.when(mockTrainerDao.create(serviceCreatedTrainer)).thenReturn(daoResponseTrainer);
        Mockito.when(mockTraineeDao.getUserCountByUserName(firstName, lastName)).thenReturn(0L);
        Mockito.when(mockTrainerDao.getUserCountByUserName(firstName, lastName)).thenReturn(0L);

        Trainer newTrainer = trainerService.createTrainer(firstName, lastName, trainingTypeString);

        assertEquals(25, newTrainer.getId());
        assertEquals(firstName, newTrainer.getFirstName());
        assertEquals(lastName, newTrainer.getLastName());
        assertEquals(userName, newTrainer.getUserName());
        assertEquals(passwordLength, newTrainer.getPassword().length());
        assertEquals(trainingType, newTrainer.getTrainingType());
    }

    @Test
    void createTrainerIfUserExistTest() {
        String expectedUserName = userName + 1;
        Trainer serviceCreatedTrainer = Trainer.builder()
                .id(0)
                .firstName(firstName)
                .lastName(lastName)
                .userName(expectedUserName)
                .password(password)
                .isActive(true)
                .trainingType(trainingType)
                .build();
        Trainer daoResponseTrainer = Trainer.builder()
                .id(25)
                .firstName(firstName)
                .lastName(lastName)
                .userName(expectedUserName)
                .password(password)
                .isActive(true)
                .trainingType(trainingType)
                .build();

        Mockito.when(mockTrainerDao.create(serviceCreatedTrainer)).thenReturn(daoResponseTrainer);
        Mockito.when(mockTraineeDao.getUserCountByUserName(firstName, lastName)).thenReturn(0L);
        Mockito.when(mockTrainerDao.getUserCountByUserName(firstName, lastName)).thenReturn(1L);

        Trainer newTrainer = trainerService.createTrainer(firstName, lastName, trainingTypeString);

        assertEquals(25, newTrainer.getId());
        assertEquals(firstName, newTrainer.getFirstName());
        assertEquals(lastName, newTrainer.getLastName());
        assertEquals(expectedUserName, newTrainer.getUserName());
        assertEquals(passwordLength, newTrainer.getPassword().length());
        assertEquals(trainingType, newTrainer.getTrainingType());

    }
}