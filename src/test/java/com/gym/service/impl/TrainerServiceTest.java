package com.gym.service.impl;

import com.gym.config.StorageConfig;
import com.gym.exceptions.IncorrectCredentialException;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.model.UserCredentials;
import com.gym.service.ITrainerService;
import com.gym.utils.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

class TrainerServiceTest {
    private final ITrainerService trainerService;
    private final String firstName = "Kerry";
    private final String lastName = "King";
    private final String userName = "Kerry.King";
    private final TrainingTypeEnum trainingTypeEnum = TrainingTypeEnum.YOGA;
    private final String trainingTypeString = "YOGA";
    private MockedStatic<StringUtils> mockStringUtil;
    private final Integer passwordLength = 10;
    private static ApplicationContext applicationContext;

    TrainerServiceTest() {
        this.trainerService = (ITrainerService) applicationContext.getBean("trainerService");
    }

    @BeforeAll
    public static void init() {
        applicationContext = new AnnotationConfigApplicationContext(StorageConfig.class);
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

        TrainerModel responseTrainerModel = trainerService.createTrainer("Tom", "Cruze", trainingTypeString);
        TrainerModel newTrainerModel = trainerService.get(responseTrainerModel.getId());

        assertEquals(responseTrainerModel.getId(), newTrainerModel.getId());
        assertEquals("Tom", newTrainerModel.getFirstName());
        assertEquals("Cruze", newTrainerModel.getLastName());
        assertEquals("Tom.Cruze", newTrainerModel.getUserName());
        assertEquals(passwordLength, newTrainerModel.getPassword().length());
        assertEquals(trainingTypeEnum, newTrainerModel.getTrainingType());
    }

    @Test
    void createTrainerIfUserExistTest() {
        String expectedUserName = userName + 1;

        TrainerModel responseTrainerModel = trainerService.createTrainer(firstName, lastName, trainingTypeString);
        TrainerModel newTrainerModel = trainerService.get(responseTrainerModel.getId());

        assertEquals(responseTrainerModel.getId(), newTrainerModel.getId());
        assertEquals(firstName, newTrainerModel.getFirstName());
        assertEquals(lastName, newTrainerModel.getLastName());
        assertEquals(expectedUserName, newTrainerModel.getUserName());
        assertEquals(passwordLength, newTrainerModel.getPassword().length());
        assertEquals(trainingTypeEnum, newTrainerModel.getTrainingType());
    }

    @Test
    void createTrainerValidationTest() {
        assertThrows(ValidationException.class, () ->
                trainerService.createTrainer(" ", lastName, trainingTypeString));
        assertThrows(ValidationException.class, () ->
                trainerService.createTrainer(firstName, " ", trainingTypeString));
    }

    @Test
    void getTrainerProfileTest() {
        TrainerModel trainerProfile;
        UserCredentials credentials = UserCredentials.builder()
                .userName("Kerry.King")
                .password("1234567890")
                .build();
        try {
            trainerProfile = trainerService.getTrainerProfile(credentials);
        } catch (IncorrectCredentialException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(trainerProfile);
        assertEquals("Kerry", trainerProfile.getFirstName());
        assertEquals("King", trainerProfile.getLastName());
        assertEquals("Kerry.King", trainerProfile.getUserName());
        assertEquals("1234567890", trainerProfile.getPassword());
        assertEquals(TrainingTypeEnum.YOGA, trainerProfile.getTrainingType());
    }

    @Test
    void getTrainerProfileIfWrongCredentialsTest() {
        UserCredentials credentials = UserCredentials.builder()
                .userName("Kerry.King")
                .password("Wl0M")
                .build();
        assertThrows(IncorrectCredentialException.class, () -> trainerService.getTrainerProfile(credentials));
    }

    @Test
    void updateTrainerPasswordTest() {
        UserCredentials credentials = UserCredentials.builder()
                .userName("Kerry.King")
                .password("1234567890")
                .build();
        TrainerModel updatedTrainer;
        try {
            trainerService.updateTrainerPassword(credentials, "123");
            credentials.setPassword("123");
            updatedTrainer = trainerService.getTrainerProfile(credentials);
            trainerService.updateTrainerPassword(credentials, "1234567890");
        } catch (IncorrectCredentialException e) {
            throw new RuntimeException(e);
        }
        assertEquals("123", updatedTrainer.getPassword());
    }
}