package com.gym.config;

import com.gym.dao.impl.TraineeDao;
import com.gym.dao.impl.TrainerDao;
import com.gym.dao.impl.TrainingDao;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingModel;
import com.gym.service.impl.TraineeService;
import com.gym.service.impl.TrainerService;
import com.gym.service.impl.TrainingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManagerFactory;
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
        Map<String, TrainingModel> trainingStorage = (Map<String, TrainingModel>) applicationContext.getBean("trainingStorage");
        assertNotNull(trainingStorage);
        assertEquals(LinkedHashMap.class, trainingStorage.getClass());
        assertEquals(2, trainingStorage.size());
        assertEquals(TrainingModel.class, trainingStorage.get("365").getClass());
    }

    @Test
    void trainerStorageBeanTest() {
        Map<String, TrainerModel> trainerStorage = (Map<String, TrainerModel>) applicationContext.getBean("trainerStorage");
        assertNotNull(trainerStorage);
        assertEquals(LinkedHashMap.class, trainerStorage.getClass());
        assertEquals(2, trainerStorage.size());
        assertEquals(TrainerModel.class, trainerStorage.get("124").getClass());
    }

    @Test
    void traineeStorageBeanTest() {
        Map<String, TraineeModel> traineeStorage = (Map<String, TraineeModel>) applicationContext.getBean("traineeStorage");
        assertNotNull(traineeStorage);
        assertEquals(LinkedHashMap.class, traineeStorage.getClass());
        assertEquals(4, traineeStorage.size());
        assertEquals(TraineeModel.class, traineeStorage.get("225").getClass());
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

    @Test
    void entityManagerFactoryTest(){
        EntityManagerFactory factory = applicationContext.getBean(EntityManagerFactory.class);
        assertNotNull(factory);
    }
}