package com.gym.service.impl;

import com.gym.config.StorageConfig;
import com.gym.exceptions.IncorrectCredentialException;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.model.UserCredentials;
import com.gym.service.ITrainerService;
import com.gym.service.IUserCredentialsService;
import com.gym.utils.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import jakarta.validation.ValidationException;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

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
    private final IUserCredentialsService mockedCredentialsService;
    private final UserCredentials credentials;

    TrainerServiceTest() {
        this.credentials = UserCredentials.builder().build();
        this.mockedCredentialsService = Mockito.mock(UserCredentialsService.class);
        this.trainerService = (ITrainerService) applicationContext.getBean("trainerService");
        Field userCredentialsServiceField = ReflectionUtils
                .findFields(TrainerService.class, f -> f.getName().equals("credentialsService"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);
        userCredentialsServiceField.setAccessible(true);
        try {
            userCredentialsServiceField.set(trainerService, mockedCredentialsService);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
    void createTrainerTest() throws IncorrectCredentialException {
        doNothing().when(mockedCredentialsService).verifyCredentials(any());
        TrainerModel responseTrainerModel = trainerService.createTrainer("Tom", "Cruze", trainingTypeString);
        TrainerModel newTrainerModel = trainerService.get(credentials, responseTrainerModel.getId());

        assertEquals(responseTrainerModel.getId(), newTrainerModel.getId());
        assertEquals("Tom", newTrainerModel.getFirstName());
        assertEquals("Cruze", newTrainerModel.getLastName());
        assertEquals("Tom.Cruze", newTrainerModel.getUserName());
        assertEquals(passwordLength, newTrainerModel.getPassword().length());
        assertEquals(trainingTypeEnum, newTrainerModel.getTrainingType());
    }

    @Test
    void createTrainerIfUserExistTest() throws IncorrectCredentialException {
        String expectedUserName = userName + 1;
        String expectedSecondUserName = userName + 2;
        doNothing().when(mockedCredentialsService).verifyCredentials(any());
        TrainerModel responseTrainerModel = trainerService.createTrainer(firstName, lastName, trainingTypeString);
        TrainerModel newTrainerModel = trainerService.get(credentials, responseTrainerModel.getId());
        TrainerModel responseSecondTrainerModel = trainerService.createTrainer(firstName, lastName, trainingTypeString);
        TrainerModel newSecondTrainerModel = trainerService.get(credentials, responseSecondTrainerModel.getId());

        assertEquals(responseTrainerModel.getId(), newTrainerModel.getId());
        assertEquals(firstName, newTrainerModel.getFirstName());
        assertEquals(lastName, newTrainerModel.getLastName());
        assertEquals(expectedUserName, newTrainerModel.getUserName());
        assertEquals(expectedSecondUserName, newSecondTrainerModel.getUserName());
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
    void credentialsVerifyingTest() throws IncorrectCredentialException {
        UserCredentials credentials = UserCredentials.builder()
                .userName("Kerry.King")
                .password("Wl0M")
                .build();
        doThrow(IncorrectCredentialException.class).when(mockedCredentialsService)
                .verifyCredentials(credentials);
        assertTrue(trainerService.isCredentialsNotMatch(credentials));
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
    void getTrainerProfileIfWrongCredentialsTest() throws IncorrectCredentialException {
        UserCredentials credentials = UserCredentials.builder()
                .userName("Kerry.King")
                .password("Wl0M")
                .build();
        doThrow(IncorrectCredentialException.class).when(mockedCredentialsService)
                .verifyCredentials(credentials);
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

    @Test
    void setActiveStatusTest(){
        UserCredentials credentials = UserCredentials.builder()
                .userName("Dave.Lombardo")
                .password("1234567890")
                .build();
        try {
            TrainerModel trainer = trainerService.getTrainerProfile(credentials);
            boolean statusBeforeChanging = trainer.getIsActive();
            trainerService.deactivate(credentials);
            TrainerModel deactivatedTrainer = trainerService.getTrainerProfile(credentials);
            trainerService.activate(credentials);
            TrainerModel activatedTrainer = trainerService.getTrainerProfile(credentials);

            assertTrue(statusBeforeChanging);
            assertFalse(deactivatedTrainer.getIsActive());
            assertTrue(activatedTrainer.getIsActive());
        } catch (IncorrectCredentialException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getTrainingListByParametersTest(){
        UserCredentials credentials = UserCredentials.builder()
                .userName("Kerry.King")
                .password("1234567890")
                .build();

        LocalDate localDateFrom = LocalDate.of(2024, 9, 11);
        LocalDate localDateTo = LocalDate.of(2024, 9, 15);
        String traineeUserName = "Bruce.Dickinson";

        List<TrainingModel> resultList = null;
        try {
            resultList = trainerService.getTrainingList(credentials, localDateFrom, localDateTo, traineeUserName);
        } catch (IncorrectCredentialException e) {
            e.printStackTrace();
        }

        assertEquals(2, resultList.size());
    }

    @Test
    void validateMethodParameters(){
        TrainerModel responseTrainerModel = trainerService.createTrainer("", "Cruze", trainingTypeString);
    }
}