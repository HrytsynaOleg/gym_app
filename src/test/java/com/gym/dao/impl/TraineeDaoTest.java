package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.config.StorageConfig;
import com.gym.dao.ITraineeDao;
import com.gym.dao.ITrainingDao;
import com.gym.model.TraineeModel;
import com.gym.model.TrainingModel;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.gym.utils.JsonUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Log4j2
class TraineeDaoTest {
    private static ApplicationContext applicationContext;
    private final ITraineeDao dao;
    private final ITrainingDao trainingDao;
    private TraineeModel serviceInputTraineeModel;

    TraineeDaoTest() {
        this.trainingDao = (ITrainingDao) applicationContext.getBean("trainingDao");
        this.dao = (ITraineeDao) applicationContext.getBean("traineeDao");
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trainee.json")) {
            this.serviceInputTraineeModel = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
        } catch (IOException ex) {
            log.error("Error reading source file");
        }
    }

    @BeforeAll
    public static void init() {
        applicationContext = new AnnotationConfigApplicationContext(StorageConfig.class);
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
        List<TrainingModel> trainingList = trainingDao.getTraineeTrainingListById(248);
        dao.delete(248);
        List<TrainingModel> trainingListAfterDelete = trainingDao.getTraineeTrainingListById(248);

        assertNull(dao.get(248));
        assertEquals(2, trainingList.size());
        assertEquals(0, trainingListAfterDelete.size());
    }

    @Test
    void getTest() {
        TraineeModel traineeModel = dao.get(248);
        assertEquals(248, traineeModel.getId());
        assertEquals("Neil", traineeModel.getFirstName());
        assertEquals("Young", traineeModel.getLastName());
        assertEquals("Neil.Young", traineeModel.getUserName());
        assertEquals("1234567890", traineeModel.getPassword());
        assertTrue(traineeModel.getIsActive());
        assertEquals("Toronto", traineeModel.getAddress());
        assertEquals(LocalDate.of(1965,7,25), traineeModel.getDateOfBirth());
    }

    @Test
    void getIfNotExistTest() {
        TraineeModel traineeModel = dao.get(230);
        assertNull(traineeModel);
    }

    @Test
    void getTraineeByUserNameTest(){
        TraineeModel traineeModel = dao.getByUserName("Neil.Young");
        assertNotNull(traineeModel);
        assertEquals("Neil", traineeModel.getFirstName());
        assertEquals("Young", traineeModel.getLastName());
        assertEquals("Neil.Young", traineeModel.getUserName());
        assertTrue(traineeModel.getIsActive());
        assertEquals("Toronto", traineeModel.getAddress());
        assertEquals(LocalDate.of(1965,7,25), traineeModel.getDateOfBirth());
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
}