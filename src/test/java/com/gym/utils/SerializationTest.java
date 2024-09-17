package com.gym.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class SerializationTest {
    @Test
    void trainingDeserializationTest(){
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("training.json")) {
            TrainingModel trainingModel = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
            assertNotNull(trainingModel);
            assertEquals(TrainingModel.class, trainingModel.getClass());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void trainerDeserializationTest(){
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trainer.json")) {
            TrainerModel trainerModel = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
            assertNotNull(trainerModel);
            assertEquals(TrainerModel.class, trainerModel.getClass());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void traineeDeserializationTest(){
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trainee.json")) {
            TraineeModel traineeModel = JsonUtils.parseInputStream(inputStream, new TypeReference<>() {
            });
            assertNotNull(traineeModel);
            assertEquals(TraineeModel.class, traineeModel.getClass());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}