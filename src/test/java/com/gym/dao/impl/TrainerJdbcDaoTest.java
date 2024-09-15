package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.config.HibernateConfig;
import com.gym.dao.ITrainerDao;
import com.gym.model.Trainer;
import com.gym.model.TrainingType;
import com.gym.utils.JsonUtils;
import com.gym.utils.StorageUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class TrainerJdbcDaoTest {
    private static ApplicationContext applicationContext;

    private final TrainerJdbcDao dao;
    private Trainer serviceInputTrainer;

    TrainerJdbcDaoTest() {
        this.dao = (TrainerJdbcDao) applicationContext.getBean("trainerJdbcDao");
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trainer.json")) {
            this.serviceInputTrainer = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
        } catch (IOException ex) {
            log.error("Error reading source file");
        }
    }

    @BeforeAll
    public static void init() {
        applicationContext = new AnnotationConfigApplicationContext(HibernateConfig.class);
    }

    @Test
    void createTrainerTest() {
        dao.create(serviceInputTrainer);
    }
}