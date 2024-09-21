package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.config.StorageConfig;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingTypeEnum;
import com.gym.utils.JsonUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class TrainerDaoTest {
    private static ApplicationContext applicationContext;

    private final TrainerDao dao;
    private TrainerModel serviceInputTrainerModel;

    TrainerDaoTest() {
        this.dao = (TrainerDao) applicationContext.getBean("trainerDao");
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trainer.json")) {
            this.serviceInputTrainerModel = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
        } catch (IOException ex) {
            log.error("Error reading resource file");
        }
    }

    @BeforeAll
    public static void init() {
        applicationContext = new AnnotationConfigApplicationContext(StorageConfig.class);
    }

    @Test
    void createTrainerTest() {
        TrainerModel newTrainer =  dao.create(serviceInputTrainerModel);
        TrainerModel checkTrainer = dao.get(newTrainer.getId());
        assertNotNull(checkTrainer);
        assertEquals(serviceInputTrainerModel.getFirstName(), checkTrainer.getFirstName());
        assertEquals(serviceInputTrainerModel.getLastName(), checkTrainer.getLastName());
        assertEquals(serviceInputTrainerModel.getUserName(), checkTrainer.getUserName());
        assertEquals(serviceInputTrainerModel.getPassword(), checkTrainer.getPassword());
        assertEquals(serviceInputTrainerModel.getIsActive(), checkTrainer.getIsActive());
        assertEquals(serviceInputTrainerModel.getTrainingType(), checkTrainer.getTrainingType());
    }

    @Test
    void getTrainerModelByIdTest(){
        TrainerModel trainerModel = dao.get(36L);
        assertNotNull(trainerModel);
        assertEquals("Kerry", trainerModel.getFirstName());
        assertEquals("King", trainerModel.getLastName());
        assertEquals("Kerry.King", trainerModel.getUserName());
        assertEquals("123456", trainerModel.getPassword());
        assertTrue(trainerModel.getIsActive());
        assertEquals(TrainingTypeEnum.YOGA, trainerModel.getTrainingType());

    }

    @Test
    void getUsersCountByUserNameTest(){
        long userCountByUserName = dao.getUserCountByUserName("Kerry", "King");
        System.out.println(userCountByUserName);
    }

    @Test
    void getTrainerIfNotExistTest(){
        TrainerModel trainerModel = dao.get(1L);
        assertNull(trainerModel);
    }


    @Test
    void getTrainerByUserNameTest(){
        TrainerModel trainerModel = dao.getByUserName("Kerry.King");
        assertNotNull(trainerModel);
        assertEquals("Kerry", trainerModel.getFirstName());
        assertEquals("King", trainerModel.getLastName());
        assertEquals("Kerry.King", trainerModel.getUserName());
        assertTrue(trainerModel.getIsActive());
        assertEquals(TrainingTypeEnum.YOGA, trainerModel.getTrainingType());
    }

    @Test
    void getTrainerByUserNameIfNotExistTest(){
        TrainerModel trainerModel = dao.getByUserName("112");
        assertNull(trainerModel);
    }

    @Test
    void updateTrainerTest(){
        TrainerModel trainerModel = dao.getByUserName("Kerry.King");
        trainerModel.setFirstName("Jan");
        trainerModel.setLastName("Holm");
        dao.update(trainerModel);
        TrainerModel updatedTrainer = dao.getByUserName("Kerry.King");

        assertEquals("Jan", updatedTrainer.getFirstName());
        assertEquals("Holm", updatedTrainer.getLastName());
    }
}