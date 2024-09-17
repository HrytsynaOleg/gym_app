package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.config.StorageConfig;
import com.gym.model.Trainer;
import com.gym.utils.JsonUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.io.InputStream;

@Log4j2
class TrainerJpaDaoTest {
    private static ApplicationContext applicationContext;

    private final TrainerJpaDao dao;
    private Trainer serviceInputTrainer;

    TrainerJpaDaoTest() {
        this.dao = (TrainerJpaDao) applicationContext.getBean("trainerJdbcDao");
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trainer.json")) {
            this.serviceInputTrainer = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
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
    void createTrainerTest() {
        dao.create(serviceInputTrainer);
    }
}