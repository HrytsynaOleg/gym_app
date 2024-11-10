package com.gym.service.impl;

import com.gym.dto.training.TrainerTrainingListItemDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.*;
import com.gym.service.ITrainerService;
import com.gym.utils.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.ValidationException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TrainerServiceTest {
    private final ITrainerService trainerService;
    private final String firstName = "Kerry";
    private final String lastName = "King";
    private final String userName = "Kerry.King";
    private final TrainingTypeEnum trainingTypeEnum = TrainingTypeEnum.YOGA;
    private final String trainingTypeString = "YOGA";
    private MockedStatic<StringUtils> mockStringUtil;
    private final Integer passwordLength = 10;

    @Autowired
    TrainerServiceTest(ITrainerService trainerService) {
        this.trainerService = trainerService;
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
    void createTrainerIfUserExistTest() throws IncorrectCredentialException {
        String expectedUserName = userName + 1;
        String expectedSecondUserName = userName + 2;
        TrainerModel responseTrainerModel = trainerService.createTrainer(firstName, lastName, trainingTypeString);
        TrainerModel newTrainerModel = trainerService.get(responseTrainerModel.getId());
        TrainerModel responseSecondTrainerModel = trainerService.createTrainer(firstName, lastName, trainingTypeString);
        TrainerModel newSecondTrainerModel = trainerService.get(responseSecondTrainerModel.getId());

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
    void getTrainerProfileTest() {
        TrainerModel trainerProfile;
        try {
            trainerProfile = trainerService.getTrainerProfile("Kerry.King");
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(trainerProfile);
        assertEquals("Kerry", trainerProfile.getFirstName());
        assertEquals("King", trainerProfile.getLastName());
        assertEquals("Kerry.King", trainerProfile.getUserName());
        assertEquals("{bcrypt}$2a$10$lPj.JYt9.Fa9mWKQj6v/4O3x4HgzEUu9AnyWIS6WtVHQ44UKKqODu",
                trainerProfile.getPassword());
        assertEquals(TrainingTypeEnum.YOGA, trainerProfile.getTrainingType());
    }

    @Test
    void setActiveStatusTest(){
        try {
            TrainerModel trainer = trainerService.getTrainerProfile("Dave.Lombardo");
            boolean statusBeforeChanging = trainer.getIsActive();
            trainerService.deactivate("Dave.Lombardo");
            TrainerModel deactivatedTrainer = trainerService.getTrainerProfile("Dave.Lombardo");
            trainerService.activate("Dave.Lombardo");
            TrainerModel activatedTrainer = trainerService.getTrainerProfile("Dave.Lombardo");

            assertTrue(statusBeforeChanging);
            assertFalse(deactivatedTrainer.getIsActive());
            assertTrue(activatedTrainer.getIsActive());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getTrainingListByParametersTest(){

        String localDateFrom = "2024-09-11";
        String localDateTo = "2024-09-15";
        String traineeUserName = "Bruce.Dickinson";

        List<TrainerTrainingListItemDTO> resultList = null;
        try {
            resultList = trainerService.getTrainingList("Kerry.King", localDateFrom, localDateTo, traineeUserName);
        } catch (IncorrectCredentialException e) {
            e.printStackTrace();
        }

        assertEquals(2, resultList.size());
    }

    @Test
    void getAssignedTraineeList(){
        List<TraineeModel> assignedTraineeList = null;
        try {
            assignedTraineeList = trainerService.getAssignedTraineeList("Tom.Arraya");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        assertEquals(3, assignedTraineeList.size());
    }
}