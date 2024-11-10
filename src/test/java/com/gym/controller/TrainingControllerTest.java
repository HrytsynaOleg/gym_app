package com.gym.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.config.WebSecurityConfig;
import com.gym.dto.training.TrainingCreateDTO;
import com.gym.model.TrainingModel;
import com.gym.service.ITrainingService;
import com.gym.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TrainingController.class)
@Import(WebSecurityConfig.class)
class TrainingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ITrainingService service;

    @Test
    @WithMockUser
    void createTrainingTest() {
        TrainingModel trainingModel = JsonUtils.parseResource("trainingCreateRestTest.json", new TypeReference<>() {
        });
        TrainingCreateDTO trainerCreateDTO = TrainingCreateDTO.builder()
                .trainerUserName("Tom.Arraya")
                .traineeUserName("James.Hetfield")
                .trainingName("test training")
                .trainingDate("2024-10-18")
                .trainingDuration(30)
                .build();
        given(service.createTraining(trainerCreateDTO)).willReturn(trainingModel);
        try {
            mvc.perform(post("/trainings").contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.convertObjectToJson(trainerCreateDTO)))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @WithMockUser
    void createTrainingWithWrongParameterTest() {
        TrainingCreateDTO trainerCreateDTO = TrainingCreateDTO.builder()
                .trainerUserName("")
                .traineeUserName("")
                .trainingName("test training")
                .trainingDate("2024-10-18")
                .trainingDuration(5)
                .build();
        try {
            mvc.perform(post("/trainings").contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.convertObjectToJson(trainerCreateDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors").isNotEmpty())
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors[?(@ =='Trainee username name must be not empty')]")
                            .value("Trainee username name must be not empty"))
                    .andExpect(jsonPath("$.errors[?(@ =='Training duration cannot be less than 10 min')]")
                            .value("Training duration cannot be less than 10 min"))
                    .andExpect(jsonPath("$.errors[?(@ =='Trainer username must be not empty')]")
                            .value("Trainer username must be not empty"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @WithMockUser
    void getTrainingTypeListTest() {
        try {
            mvc.perform(get("/trainings/types"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}