package com.gym.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dto.training.TrainingCreateDTO;
import com.gym.model.TrainingModel;
import com.gym.service.ITrainingService;
import com.gym.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TrainingController.class)
class TrainingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ITrainingService service;

    @Test
    void createTrainingTest() {
        TrainingModel trainingModel = JsonUtils.parseResource("trainingCreateRestTest.json", new TypeReference<>() {
        });
        TrainingCreateDTO trainerCreateDTO = TrainingCreateDTO.builder()
                .trainerUserName("Tom.Arraya")
                .traineeUserName("James.Hetfield")
                .trainingName("test training")
                .trainingDate("2024-10-18")
                .trainingDuration("30")
                .build();
        given(service.createTraining(trainerCreateDTO)).willReturn(trainingModel);
        try {
            mvc.perform(post("/training").contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.convertObjectToJson(trainerCreateDTO)))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}