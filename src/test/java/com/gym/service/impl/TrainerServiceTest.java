package com.gym.service.impl;

import com.gym.model.Trainer;
import com.gym.model.TrainingType;
import com.gym.service.ITrainerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TrainerServiceTest {
    @Autowired
    ITrainerService trainerService;

    @Test
    void create() {
        String firstName = "Andriy";
        String lastName = "Dydenko";
        long id = trainerService.create(firstName, lastName, TrainingType.FITNESS);
        Trainer trainer = trainerService.getById(id);
        assertEquals(firstName, trainer.getFirstName());
        assertEquals(lastName, trainer.getLastName());
        assertEquals(TrainingType.FITNESS, trainer.getTrainingType());
    }

    @Test
    void update() {
    }

    @Test
    void getById() {
    }
}