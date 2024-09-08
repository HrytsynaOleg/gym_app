package com.gym.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.model.Trainee;
import com.gym.model.Trainer;
import com.gym.model.Training;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class SerializationTest {
    @Test
    void trainingDeserializationTest(){
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("training.json")) {
            Training training = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
            assertNotNull(training);
            assertEquals(Training.class, training.getClass());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void trainerDeserializationTest(){
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trainer.json")) {
            Trainer trainer = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
            assertNotNull(trainer);
            assertEquals(Trainer.class, trainer.getClass());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void traineeDeserializationTest(){
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trainee.json")) {
            Trainee trainee = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
            assertNotNull(trainee);
            assertEquals(Trainee.class, trainee.getClass());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}