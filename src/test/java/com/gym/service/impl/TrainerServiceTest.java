package com.gym.service.impl;

import com.gym.model.Trainer;
import com.gym.model.TrainingType;
import com.gym.service.ITrainerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class TrainerServiceTest {
    @Autowired
    ITrainerService trainerService;

    @Test
    void createTrainerTest() {
        String firstName = "Andriy";
        String lastName = "Dydenko";
        Trainer newTrainer = trainerService.createTrainer(firstName, lastName, TrainingType.FITNESS);
        Trainer trainer = trainerService.getById(newTrainer.getId());
        assertEquals(firstName, trainer.getFirstName());
        assertEquals(lastName, trainer.getLastName());
        assertEquals("Andriy.Dydenko", trainer.getUserName());
        assertEquals(TrainingType.FITNESS, trainer.getTrainingType());
        assertEquals(10, trainer.getPassword().length());
    }

    @Test
    void createTrainerIfUserExistTest() {
        String firstName = "Pavlo";
        String lastName = "Petrenko";
        Trainer newTrainer = trainerService.createTrainer(firstName, lastName, TrainingType.ZUMBA);
        Trainer trainer = trainerService.getById(newTrainer.getId());
        assertEquals(firstName, trainer.getFirstName());
        assertEquals(lastName, trainer.getLastName());
        assertEquals("Pavlo.Petrenko1", trainer.getUserName());
        assertEquals(TrainingType.ZUMBA, trainer.getTrainingType());
    }

    @Test
    void createTrainerIfUserExistInTraineesTest() {
        String firstName = "Serhiy";
        String lastName = "Volovets";
        Trainer newTrainer = trainerService.createTrainer(firstName, lastName, TrainingType.ZUMBA);
        Trainer trainer = trainerService.getById(newTrainer.getId());
        assertEquals(firstName, trainer.getFirstName());
        assertEquals(lastName, trainer.getLastName());
        assertEquals("Serhiy.Volovets1", trainer.getUserName());
        assertEquals(TrainingType.ZUMBA, trainer.getTrainingType());
    }

    @Test
    void updateTrainerTest() {
        Trainer trainer = trainerService.getById(124);
        assertEquals("Petrenko", trainer.getLastName());
        trainer.setLastName("Melnyk");
        trainerService.update(trainer);
        Trainer updatedTrainer = trainerService.getById(124);
        assertEquals("Melnyk", updatedTrainer.getLastName());
    }

    @Test
    void getTrainerByIdTest() {
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