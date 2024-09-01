package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dao.ITrainerDao;
import com.gym.model.Trainer;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
class TrainerDaoTest {
    @Autowired
    ITrainerDao dao;

    @Test
    void addNew() {
        ClassLoader classLoader = getClass().getClassLoader();
        Trainer testTrainer;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trainer.json")) {
            String json = JsonPath.parse(inputStream).jsonString();
            testTrainer = JsonUtils.parseJsonString(json, new TypeReference<>() {
            });
            long id = dao.addNew(testTrainer);
            Trainer newTrainer = dao.getById(id);

        } catch (IOException ex) {
            System.err.println(ex.getCause());
        }
    }

    @Test
    void update() {
    }

    @Test
    void getById() {
    }
}