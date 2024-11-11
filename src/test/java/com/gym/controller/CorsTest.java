package com.gym.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.model.TraineeModel;
import com.gym.service.ITraineeService;
import com.gym.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CorsTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ITraineeService service;

    @Test
    @WithMockUser(value = "Bruce.Dickinson")
    void performAllowedOriginTest() {
        TraineeModel traineeModel = JsonUtils.parseResource("traineeRestTest.json", new TypeReference<>() {
        });
        try {
            given(service.getTraineeProfile("Bruce.Dickinson")).willReturn(traineeModel);
            given(service.getAssignedTrainerList("Bruce.Dickinson")).willReturn(List.of());

            mvc.perform(get("/trainees/Bruce.Dickinson")
                            .header("Origin", "https://localhost:8888"))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Access-Control-Allow-Origin", "https://localhost:8888"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(value = "Bruce.Dickinson")
    void performDeniedOriginTest() {
        TraineeModel traineeModel = JsonUtils.parseResource("traineeRestTest.json", new TypeReference<>() {
        });
        try {
            given(service.getTraineeProfile("Bruce.Dickinson")).willReturn(traineeModel);
            given(service.getAssignedTrainerList("Bruce.Dickinson")).willReturn(List.of());

            mvc.perform(get("/trainees/Bruce.Dickinson")
                            .header("Origin", "https://something.com"))
                    .andExpect(status().isForbidden())
                    .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
