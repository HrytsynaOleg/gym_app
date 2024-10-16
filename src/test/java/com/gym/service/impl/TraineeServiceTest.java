package com.gym.service.impl;

import com.gym.dto.training.TraineeTrainingListItemDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.model.UserCredentials;
import com.gym.service.ITraineeService;
import com.gym.service.ITrainerService;
import com.gym.service.IUserCredentialsService;
import com.gym.utils.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.ValidationException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
class TraineeServiceTest {
    private final ITraineeService traineeService;
    private final ITrainerService trainerService;
    private final String address = "Los Angeles";
    private final String dateOfBirthInString = "1999-02-15";
    private final LocalDate dateOfBirth;
    private final String firstName = "Valeriy";
    private final String lastName = "Tokar";
    private final String userName = "Valeriy.Tokar";
    private final Integer passwordLength = 10;
    private MockedStatic<StringUtils> mockStringUtil;
    private final IUserCredentialsService mockedCredentialsService;
    private final UserCredentials credentials;

    @Autowired
    public TraineeServiceTest(ITraineeService traineeService, ITrainerService trainerService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.credentials = UserCredentials.builder().build();
        this.mockedCredentialsService = Mockito.mock(UserCredentialsService.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateOfBirth = LocalDate.parse(dateOfBirthInString, formatter);
        Field userCredentialsServiceField = ReflectionUtils
                .findFields(TraineeService.class, f -> f.getName().equals("credentialsService"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);
        userCredentialsServiceField.setAccessible(true);
        try {
            userCredentialsServiceField.set(traineeService, mockedCredentialsService);
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
    void createTraineeTest() throws IncorrectCredentialException {
        doNothing().when(mockedCredentialsService).verifyCredentials(any());
        TraineeModel responseTraineeModel = traineeService.createTrainee(firstName, lastName, address, dateOfBirthInString);
        TraineeModel newTraineeModel = traineeService.get(credentials, responseTraineeModel.getId());

        assertEquals(responseTraineeModel.getId(), newTraineeModel.getId());
        assertEquals(firstName, newTraineeModel.getFirstName());
        assertEquals(lastName, newTraineeModel.getLastName());
        assertEquals(userName, newTraineeModel.getUserName());
        assertEquals(passwordLength, newTraineeModel.getPassword().length());
        assertEquals(address, newTraineeModel.getAddress());
        assertEquals(dateOfBirth, newTraineeModel.getDateOfBirth());
    }

    @Test
    void createTraineeIfUserExistTest() throws IncorrectCredentialException {
        String testFirstName = "Andriy";
        String testLastName = "Kolos";
        String testUserName = "Andriy.Kolos";
        String expectedUserName = testUserName + 1;
        String expectedSecondUserName = testUserName + 2;
        doNothing().when(mockedCredentialsService).verifyCredentials(any());
        TraineeModel responseTraineeModel = traineeService.createTrainee(testFirstName, testLastName, address, dateOfBirthInString);
        TraineeModel newTraineeModel = traineeService.get(credentials, responseTraineeModel.getId());
        TraineeModel responseSecondTraineeModel = traineeService.createTrainee(testFirstName, testLastName, address, dateOfBirthInString);
        TraineeModel newSecondTraineeModel = traineeService.get(credentials ,responseSecondTraineeModel.getId());
        TraineeModel responseThirdTraineeModel = traineeService.createTrainee(testFirstName, testLastName, address, dateOfBirthInString);
        TraineeModel newThirdTraineeModel = traineeService.get(credentials, responseThirdTraineeModel.getId());

        assertEquals(responseTraineeModel.getId(), newTraineeModel.getId());
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
    void credentialsVerifyingTest() throws IncorrectCredentialException {
        UserCredentials credentials = UserCredentials.builder()
                .userName("Kerry.King")
                .password("Wl0M")
                .build();
        doThrow(IncorrectCredentialException.class).when(mockedCredentialsService)
                .verifyCredentials(credentials);
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
        assertEquals(LocalDate.of(1965, 7, 25), traineeProfile.getDateOfBirth());
    }

    @Test
    void setActiveStatusTest() {
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
    void deleteTraineeTest() {
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
    @Test
    void getTrainingListByParametersTest(){
        UserCredentials credentials = UserCredentials.builder()
                .userName("Bruce.Dickinson")
                .password("1234567890")
                .build();

        String localDateFrom = "2024-09-11";
        String localDateTo = "2024-09-15";
        String trainerUserName = "Kerry.King";

        List<TraineeTrainingListItemDTO> resultList = null;
        try {
            resultList = traineeService.getTrainingList(credentials, localDateFrom, localDateTo,
                    trainerUserName, "YOGA");
        } catch (IncorrectCredentialException e) {
            e.printStackTrace();
        }

        assertEquals(2, resultList.size());
    }

    @Test
    void updateTrainerListTest() throws IncorrectCredentialException {
        UserCredentials credentials = UserCredentials.builder()
                .userName("Bruce.Dickinson")
                .password("1234567890")
                .build();
        List<String> newTrainerList = new ArrayList<>();
        newTrainerList.add(trainerService.get(credentials, 117).getUserName());
        newTrainerList.add(trainerService.get(credentials, 118).getUserName());
        List<TrainerModel> trainerModelList = traineeService.getAssignedTrainerList(credentials);
        traineeService.updateTrainerList(credentials, newTrainerList);
        List<TrainerModel> trainerModelUpdatedList = traineeService.getAssignedTrainerList(credentials);

        assertEquals(1, trainerModelList.size());
        assertEquals(2, trainerModelUpdatedList.size());
    }

}