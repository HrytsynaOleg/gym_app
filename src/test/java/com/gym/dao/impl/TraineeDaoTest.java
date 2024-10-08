package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dao.ITraineeDao;
import com.gym.dao.ITrainerDao;
import com.gym.dao.ITrainingDao;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingModel;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import com.gym.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Log4j2
@SpringBootTest
class TraineeDaoTest {
    private final ITraineeDao dao;
    private final ITrainingDao trainingDao;
    private final ITrainerDao trainerDao;
    private final TraineeModel serviceInputTraineeModel;

    @Autowired
    TraineeDaoTest(ITraineeDao dao, ITrainingDao trainingDao, ITrainerDao trainerDao) {
        this.dao = dao;
        this.trainingDao = trainingDao;
        this.trainerDao = trainerDao;
        this.serviceInputTraineeModel = JsonUtils.parseResource("trainee.json", new TypeReference<>() {
        });
    }

    @Test
    void createTraineeTest() {
            TraineeModel newTraineeModel = dao.create(serviceInputTraineeModel);
            TraineeModel testNewTraineeModel = dao.get(newTraineeModel.getId());

            assertNotNull(testNewTraineeModel);
            assertEquals(newTraineeModel.getId(), testNewTraineeModel.getId());
            assertEquals(serviceInputTraineeModel.getFirstName(), testNewTraineeModel.getFirstName());
            assertEquals(serviceInputTraineeModel.getLastName(), testNewTraineeModel.getLastName());
            assertEquals(serviceInputTraineeModel.getUserName(), testNewTraineeModel.getUserName());
            assertEquals(serviceInputTraineeModel.getPassword(), testNewTraineeModel.getPassword());
            assertTrue(serviceInputTraineeModel.getIsActive());
            assertEquals(serviceInputTraineeModel.getAddress(), testNewTraineeModel.getAddress());
            assertEquals(serviceInputTraineeModel.getDateOfBirth(), testNewTraineeModel.getDateOfBirth());
    }

    @Test
    void deleteTraineeTest() {
        List<TrainingModel> trainingList = trainingDao.getTraineeTrainingListById(253);
        dao.delete(253);
        List<TrainingModel> trainingListAfterDelete = trainingDao.getTraineeTrainingListById(253);

        assertNull(dao.get(253));
        assertEquals(1, trainingList.size());
        assertEquals(0, trainingListAfterDelete.size());
    }

    @Test
    void getTest() {
        TraineeModel traineeModel = dao.get(253);
        assertEquals(253, traineeModel.getId());
        assertEquals("Ozzy", traineeModel.getFirstName());
        assertEquals("Osbourne", traineeModel.getLastName());
        assertEquals("Ozzy.Osbourne", traineeModel.getUserName());
        assertEquals("1234567890", traineeModel.getPassword());
        assertTrue(traineeModel.getIsActive());
        assertEquals("Boston", traineeModel.getAddress());
        assertEquals(LocalDate.of(1978,2,3), traineeModel.getDateOfBirth());
    }

    @Test
    void getIfNotExistTest() {
        TraineeModel traineeModel = dao.get(230);
        assertNull(traineeModel);
    }

    @Test
    void getTraineeByUserNameTest(){
        TraineeModel traineeModel = dao.getByUserName("Ozzy.Osbourne");
        assertNotNull(traineeModel);
        assertEquals("Ozzy", traineeModel.getFirstName());
        assertEquals("Osbourne", traineeModel.getLastName());
        assertEquals("Ozzy.Osbourne", traineeModel.getUserName());
        assertTrue(traineeModel.getIsActive());
        assertEquals("Boston", traineeModel.getAddress());
        assertEquals(LocalDate.of(1978,2,3), traineeModel.getDateOfBirth());
    }

    @Test
    void getTraineeByUserNameIfNotExistTest(){
        TraineeModel traineeModel = dao.getByUserName("112");
        assertNull(traineeModel);
    }

    @Test
    void updateTraineeTest(){
        TraineeModel traineeModel = dao.getByUserName("Kirk.Hammett");
        traineeModel.setFirstName("Jan");
        traineeModel.setLastName("Holm");
        dao.update(traineeModel);
        TraineeModel updatedTrainee = dao.getByUserName("Kirk.Hammett");

        assertEquals("Jan", updatedTrainee.getFirstName());
        assertEquals("Holm", updatedTrainee.getLastName());
    }

    @Test
    void getIntendedTrainersTest(){
        TraineeModel traineeModel = dao.get(253);
        List<TrainerModel> intendedTrainerList = dao.getIntendedTrainerList(traineeModel);

        assertEquals(1, intendedTrainerList.size());
    }

    @Test
    void intendTrainerTest(){
        TraineeModel traineeModel = dao.get(253);
        TrainerModel trainerModel = trainerDao.get(118);
        List<TrainerModel> intendedTrainerList = dao.getIntendedTrainerList(traineeModel);
        dao.intendTrainer(traineeModel, trainerModel);
        List<TrainerModel> intendedTrainerListAfterIntending = dao.getIntendedTrainerList(traineeModel);

        assertEquals(1, intendedTrainerList.size());
        assertEquals(2, intendedTrainerListAfterIntending.size());
    }

    @Test
    void deleteTrainerTest(){
        TraineeModel traineeModel = dao.get(258);
        TrainerModel trainerModel = trainerDao.get(117);
        List<TrainerModel> intendedTrainerList = dao.getIntendedTrainerList(traineeModel);
        dao.deleteTrainer(traineeModel, trainerModel);
        List<TrainerModel> intendedTrainerListAfterDeleting = dao.getIntendedTrainerList(traineeModel);

        assertEquals(1, intendedTrainerList.size());
        assertEquals(0, intendedTrainerListAfterDeleting.size());
    }
}