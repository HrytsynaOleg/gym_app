package com.gym.service.impl;

import com.gym.config.StorageConfig;
import com.gym.exceptions.IncorrectCredentialException;
import com.gym.model.TraineeModel;
import com.gym.model.UserCredentials;
import com.gym.service.ITraineeService;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TraineeServiceTest {
    private final ITraineeService traineeService;
    private final String address = "Los Angeles";
    private final String dateOfBirthInString = "1999-02-15";
    private final LocalDate dateOfBirth;
    private final String firstName = "Valeriy";
    private final String lastName = "Tokar";
    private final String userName = "Valeriy.Tokar";
    private final String password = "1234567890";
    private final Integer passwordLength = 10;
    private MockedStatic<StringUtils> mockStringUtil;
    private static ApplicationContext applicationContext;

    public TraineeServiceTest() {
        this.traineeService = (ITraineeService) applicationContext.getBean("traineeService");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateOfBirth = LocalDate.parse(dateOfBirthInString, formatter);
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
    void createTraineeTest() {

        TraineeModel responseTraineeModel = traineeService.createTrainee(firstName, lastName, address, dateOfBirthInString);
        TraineeModel newTraineeModel = traineeService.get(responseTraineeModel.getId());

        assertEquals(responseTraineeModel.getId(), newTraineeModel.getId());
        assertEquals(firstName, newTraineeModel.getFirstName());
        assertEquals(lastName, newTraineeModel.getLastName());
        assertEquals(userName, newTraineeModel.getUserName());
        assertEquals(passwordLength, newTraineeModel.getPassword().length());
        assertEquals(address, newTraineeModel.getAddress());
        assertEquals(dateOfBirth, newTraineeModel.getDateOfBirth());
    }

    @Test
    void createTraineeIfUserExistTest() {
        String testFirstName = "Andriy";
        String testLastName = "Kolos";
        String testUserName = "Andriy.Kolos";
        String expectedUserName = testUserName + 1;
        String expectedSecondUserName = testUserName + 2;

        TraineeModel responseTraineeModel = traineeService.createTrainee(testFirstName, testLastName, address, dateOfBirthInString);
        TraineeModel newTraineeModel = traineeService.get(responseTraineeModel.getId());
        TraineeModel responseSecondTraineeModel = traineeService.createTrainee(testFirstName, testLastName, address, dateOfBirthInString);
        TraineeModel newSecondTraineeModel = traineeService.get(responseSecondTraineeModel.getId());
        TraineeModel responseThirdTraineeModel = traineeService.createTrainee(testFirstName, testLastName, address, dateOfBirthInString);
        TraineeModel newThirdTraineeModel = traineeService.get(responseThirdTraineeModel.getId());

        assertEquals(newTraineeModel.getId(), newTraineeModel.getId());
        assertEquals(testFirstName, newTraineeModel.getFirstName());
        assertEquals(testLastName, newTraineeModel.getLastName());
        assertEquals(testUserName, newTraineeModel.getUserName());
        assertEquals(expectedUserName, newSecondTraineeModel.getUserName());
        assertEquals(expectedSecondUserName, newThirdTraineeModel.getUserName());
        assertEquals(passwordLength, newTraineeModel.getPassword().length());
        assertEquals(address, newTraineeModel.getAddress());
        assertEquals(dateOfBirth, newTraineeModel.getDateOfBirth());
    }

    @Test
    void createTraineeValidationTest() {
        assertThrows(ValidationException.class, () ->
                traineeService.createTrainee(" ", lastName, address, dateOfBirthInString));
        assertThrows(ValidationException.class, () ->
                traineeService.createTrainee(firstName, " ", address, dateOfBirthInString));
    }

    @Test
    void credentialsVerifyingTest(){
        UserCredentials credentials = UserCredentials.builder()
                .userName("Kerry.King")
                .password("Wl0M")
                .build();
        assertTrue(traineeService.isCredentialsNotMatch(credentials));
    }

    @Test
    void getTrainerProfileTest() {
        TraineeModel traineeProfile;
        UserCredentials credentials = UserCredentials.builder()
                .userName("Neil.Young")
                .password("1234567890")
                .build();
        try {
            traineeProfile = traineeService.getTraineeProfile(credentials);
        } catch (IncorrectCredentialException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(traineeProfile);
        assertEquals("Neil", traineeProfile.getFirstName());
        assertEquals("Young", traineeProfile.getLastName());
        assertEquals("Neil.Young", traineeProfile.getUserName());
        assertEquals("1234567890", traineeProfile.getPassword());
        assertEquals("Toronto", traineeProfile.getAddress());
        assertEquals(LocalDate.of(1965,7,25), traineeProfile.getDateOfBirth());
    }

    @Test
    void setActiveStatusTest(){
        UserCredentials credentials = UserCredentials.builder()
                .userName("David.Gilmoure")
                .password("1234567890")
                .build();
        try {
            TraineeModel trainee = traineeService.getTraineeProfile(credentials);
            boolean statusBeforeChanging = trainee.getIsActive();
            traineeService.deactivate(credentials);
            TraineeModel deactivatedTrainee = traineeService.getTraineeProfile(credentials);
            traineeService.activate(credentials);
            TraineeModel activatedTrainee = traineeService.getTraineeProfile(credentials);

            assertTrue(statusBeforeChanging);
            assertFalse(deactivatedTrainee.getIsActive());
            assertTrue(activatedTrainee.getIsActive());
        } catch (IncorrectCredentialException e) {
            e.printStackTrace();
        }
    }

    @Test
    void updateTraineePasswordTest() {
        UserCredentials credentials = UserCredentials.builder()
                .userName("David.Gilmoure")
                .password("1234567890")
                .build();
        TraineeModel updatedTrainee;
        try {
            traineeService.updateTraineePassword(credentials, "123");
            credentials.setPassword("123");
            updatedTrainee = traineeService.getTraineeProfile(credentials);
            traineeService.updateTraineePassword(credentials, "1234567890");
        } catch (IncorrectCredentialException e) {
            throw new RuntimeException(e);
        }
        assertEquals("123", updatedTrainee.getPassword());
    }

    @Test
    void deleteTraineeTest(){
        UserCredentials credentials = UserCredentials.builder()
                .userName("Neil.Young")
                .password("1234567890")
                .build();
        try {
            traineeService.delete(credentials);
            TraineeModel traineeModelAfterDelete = traineeService.getTraineeProfile(credentials);

            assertNull(traineeModelAfterDelete);
        } catch (IncorrectCredentialException e) {
            throw new RuntimeException(e);
        }

    }
}