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
    void createTrainerTrainerTest() {
        String firstName = "Andriy";
        String lastName = "Dydenko";
        Trainer newTrainer = trainerService.createTrainer(firstName, lastName, TrainingType.FITNESS);
        Trainer trainer = trainerService.getById(newTrainer.getId());
        assertEquals(firstName, trainer.getFirstName());
        assertEquals(lastName, trainer.getLastName());
        assertEquals(TrainingType.FITNESS, trainer.getTrainingType());
    }

    @Test
    void createTrainerTrainerIfUserExistTest() {
        String firstName = "Pavlo";
        String lastName = "Petrenko";
        Trainer newTrainer = trainerService.createTrainer(firstName, lastName, TrainingType.ZUMBA);
        Trainer trainer = trainerService.getById(newTrainer.getId());
        assertEquals("Pavlo.Petrenko1", trainer.getUserName());
        assertEquals(TrainingType.ZUMBA, trainer.getTrainingType());
    }

    @Test
    void createTrainerTrainerIfUserExistInTraineesTest() {
        String firstName = "Serhiy";
        String lastName = "Volovets";
        Trainer newTrainer = trainerService.createTrainer(firstName, lastName, TrainingType.ZUMBA);
        Trainer trainer = trainerService.getById(newTrainer.getId());
        assertEquals("Serhiy.Volovets1", trainer.getUserName());
        assertEquals(TrainingType.ZUMBA, trainer.getTrainingType());
    }

    @Test
    void updateTrainerTest() {
        Trainer trainer = trainerService.getById(125);
        assertEquals(TrainingType.YOGA, trainer.getTrainingType());
        trainer.setTrainingType(TrainingType.STRETCHING);
        trainerService.update(trainer);
        Trainer updatedTrainer = trainerService.getById(125);
        assertEquals(TrainingType.STRETCHING, updatedTrainer.getTrainingType());
    }

    @Test
    void getByIdTest() {
        Trainer trainer = trainerService.getById(125);
        assertEquals(125, trainer.getId());
        assertEquals("Dmytro", trainer.getFirstName());
        assertEquals("Sirenko", trainer.getLastName());
        assertEquals("Dmytro.Sirenko", trainer.getUserName());
        assertEquals("123456", trainer.getPassword());
        assertEquals(TrainingType.YOGA, trainer.getTrainingType());
        assertTrue(trainer.getIsActive());
    }
}