package com.gym.service.impl;

import com.gym.config.StorageConfig;
import com.gym.dao.impl.TrainerDao;
import com.gym.exceptions.IncorrectCredentialException;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.model.UserCredentials;
import com.gym.service.ITrainerService;
import com.gym.utils.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.validation.ValidationException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class TrainerServiceTest {
    private final ITrainerService trainerService;
    private final TrainerDao mockTrainerDao;
    private final String firstName = "Kerry";
    private final String lastName = "King";
    private final String userName = "Kerry.King";
    private final String password = "1234567890";
    private final TrainingTypeEnum trainingTypeEnum = TrainingTypeEnum.YOGA;
    private final String trainingTypeString = "YOGA";
    private MockedStatic<StringUtils> mockStringUtil;
    private final Integer passwordLength = 10;

    TrainerServiceTest() {
        this.trainerService = new TrainerService();
        this.mockTrainerDao = Mockito.mock(TrainerDao.class);
        Field trainerDaoField = ReflectionUtils
                .findFields(TrainerService.class, f -> f.getName().equals("trainerDao"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);
        trainerDaoField.setAccessible(true);
        Field passwordLengthField = ReflectionUtils
                .findFields(TrainerService.class, f -> f.getName().equals("passwordLength"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);
        passwordLengthField.setAccessible(true);
        try {
            trainerDaoField.set(trainerService, mockTrainerDao);
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
        TrainerModel serviceCreatedTrainerModel = TrainerModel.builder()
                .id(0)
                .firstName(firstName)
                .lastName(lastName)
                .userName(userName)
                .password(password)
                .isActive(true)
                .trainingType(trainingTypeEnum)
                .build();
        TrainerModel daoResponseTrainerModel = TrainerModel.builder()
                .id(25)
                .firstName(firstName)
                .lastName(lastName)
                .userName(userName)
                .password(password)
                .isActive(true)
                .trainingType(trainingTypeEnum)
                .build();

        Mockito.when(mockTrainerDao.create(serviceCreatedTrainerModel)).thenReturn(daoResponseTrainerModel);
        Mockito.when(mockTrainerDao.getUserCountByUserName(firstName, lastName)).thenReturn(0L);

        TrainerModel newTrainerModel = trainerService.createTrainer(firstName, lastName, trainingTypeString);

        assertEquals(25, newTrainerModel.getId());
        assertEquals(firstName, newTrainerModel.getFirstName());
        assertEquals(lastName, newTrainerModel.getLastName());
        assertEquals(userName, newTrainerModel.getUserName());
        assertEquals(passwordLength, newTrainerModel.getPassword().length());
        assertEquals(trainingTypeEnum, newTrainerModel.getTrainingType());
    }

    @Test
    void createTrainerIfUserExistTest() {
        String expectedUserName = userName + 1;
        TrainerModel serviceCreatedTrainerModel = TrainerModel.builder()
                .id(0)
                .firstName(firstName)
                .lastName(lastName)
                .userName(expectedUserName)
                .password(password)
                .isActive(true)
                .trainingType(trainingTypeEnum)
                .build();
        TrainerModel daoResponseTrainerModel = TrainerModel.builder()
                .id(25)
                .firstName(firstName)
                .lastName(lastName)
                .userName(expectedUserName)
                .password(password)
                .isActive(true)
                .trainingType(trainingTypeEnum)
                .build();

        Mockito.when(mockTrainerDao.create(serviceCreatedTrainerModel)).thenReturn(daoResponseTrainerModel);
        Mockito.when(mockTrainerDao.getUserCountByUserName(firstName, lastName)).thenReturn(1L);

        TrainerModel newTrainerModel = trainerService.createTrainer(firstName, lastName, trainingTypeString);

        assertEquals(25, newTrainerModel.getId());
        assertEquals(firstName, newTrainerModel.getFirstName());
        assertEquals(lastName, newTrainerModel.getLastName());
        assertEquals(expectedUserName, newTrainerModel.getUserName());
        assertEquals(passwordLength, newTrainerModel.getPassword().length());
        assertEquals(trainingTypeEnum, newTrainerModel.getTrainingType());
    }

    @Test
    void createTrainerValidationTest() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(StorageConfig.class);
        ITrainerService contextTrainerService = applicationContext.getBean(TrainerService.class);
        assertThrows(ValidationException.class, () ->
                contextTrainerService.createTrainer(" ", lastName, trainingTypeString));
        assertThrows(ValidationException.class, () ->
                contextTrainerService.createTrainer(firstName, " ", trainingTypeString));
    }

    @Test
    void getTrainerProfileTest() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(StorageConfig.class);
        ITrainerService contextTrainerService = applicationContext.getBean(TrainerService.class);
        TrainerModel trainerProfile;
        UserCredentials credentials = UserCredentials.builder()
                .userName("Kerry.King")
                .password("Wl0MyDxPTk")
                .build();
        try {
            trainerProfile = contextTrainerService.getTrainerProfile(credentials);
        } catch (IncorrectCredentialException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(trainerProfile);
        assertEquals("Kerry", trainerProfile.getFirstName());
        assertEquals("King", trainerProfile.getLastName());
        assertEquals("Kerry.King", trainerProfile.getUserName());
        assertEquals("Wl0MyDxPTk", trainerProfile.getPassword());
        assertEquals(TrainingTypeEnum.YOGA, trainerProfile.getTrainingType());
    }

    @Test
    void getTrainerProfileIfWrongCredentialsTest() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(StorageConfig.class);
        ITrainerService contextTrainerService = applicationContext.getBean(TrainerService.class);
        UserCredentials credentials = UserCredentials.builder()
                .userName("Kerry.King")
                .password("Wl0M")
                .build();
        assertThrows(IncorrectCredentialException.class, () -> {
            contextTrainerService.getTrainerProfile(credentials);
        });
    }

    @Test
    void updateTrainerPasswordTest() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(StorageConfig.class);
        ITrainerService contextTrainerService = applicationContext.getBean(TrainerService.class);
        UserCredentials credentials = UserCredentials.builder()
                .userName("Kerry.King")
                .password("Wl0MyDxPTk")
                .build();
        TrainerModel updatedTrainer;
        try {
            contextTrainerService.updateTrainerPassword(credentials, "123");
            credentials.setPassword("123");
            updatedTrainer = contextTrainerService.getTrainerProfile(credentials);
            contextTrainerService.updateTrainerPassword(credentials, "Wl0MyDxPTk");
        } catch (IncorrectCredentialException e) {
            throw new RuntimeException(e);
        }

        assertEquals("123", updatedTrainer.getPassword());


    }
}