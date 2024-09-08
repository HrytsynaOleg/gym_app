package com.gym.config;

import com.gym.dao.impl.TraineeDao;
import com.gym.dao.impl.TrainerDao;
import com.gym.dao.impl.TrainingDao;
import com.gym.model.Trainee;
import com.gym.model.Trainer;
import com.gym.model.Training;
import com.gym.service.impl.TraineeService;
import com.gym.service.impl.TrainerService;
import com.gym.service.impl.TrainingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StorageConfigTest {
    private static ApplicationContext applicationContext;

    @BeforeAll
    public static void init() {
        applicationContext = new AnnotationConfigApplicationContext(StorageConfig.class);
    }

    @Test
    void trainingStorageBeanTest() {
        Map<String, Training> trainingStorage = (Map<String, Training>) applicationContext.getBean("trainingStorage");
        assertNotNull(trainingStorage);
        assertEquals(LinkedHashMap.class, trainingStorage.getClass());
        assertEquals(2, trainingStorage.size());
        assertEquals(Training.class, trainingStorage.get("365").getClass());
    }

    @Test
    void trainerStorageBeanTest() {
        Map<String, Trainer> trainerStorage = (Map<String, Trainer>) applicationContext.getBean("trainerStorage");
        assertNotNull(trainerStorage);
        assertEquals(LinkedHashMap.class, trainerStorage.getClass());
        assertEquals(2, trainerStorage.size());
        assertEquals(Trainer.class, trainerStorage.get("124").getClass());
    }

    @Test
    void traineeStorageBeanTest() {
        Map<String, Trainee> traineeStorage = (Map<String, Trainee>) applicationContext.getBean("traineeStorage");
        assertNotNull(traineeStorage);
        assertEquals(LinkedHashMap.class, traineeStorage.getClass());
        assertEquals(4, traineeStorage.size());
        assertEquals(Trainee.class, traineeStorage.get("225").getClass());
    }

    @Test
    void trainerDaoBeanTest(){
        TrainerDao trainerDao = applicationContext.getBean(TrainerDao.class);
        assertNotNull(trainerDao);
        assertEquals(TrainerDao.class, trainerDao.getClass());
    }

    @Test
    void traineeDaoBeanTest(){
        TraineeDao traineeDao = applicationContext.getBean(TraineeDao.class);
        assertNotNull(traineeDao);
        assertEquals(TraineeDao.class, traineeDao.getClass());
    }

    @Test
    void trainingDaoBeanTest(){
        TrainingDao trainingDao = applicationContext.getBean(TrainingDao.class);
        assertNotNull(trainingDao);
        assertEquals(TrainingDao.class, trainingDao.getClass());
    }

    @Test
    void trainerServiceBeanTest(){
        TrainerService trainerService = applicationContext.getBean(TrainerService.class);
        assertNotNull(trainerService);
        assertEquals(TrainerService.class, trainerService.getClass());
    }

    @Test
    void traineeServiceBeanTest(){
        TraineeService traineeService = applicationContext.getBean(TraineeService.class);
        assertNotNull(traineeService);
        assertEquals(TraineeService.class, traineeService.getClass());
    }

    @Test
    void trainingServiceBeanTest(){
        TrainingService trainingService = applicationContext.getBean(TrainingService.class);
        assertNotNull(trainingService);
        assertEquals(TrainingService.class, trainingService.getClass());
    }
}