package com.gym.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dto.trainer.TrainerCreateDTO;
import com.gym.dto.trainer.TrainerUpdateDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.TrainerModel;
import com.gym.service.ITrainerService;
import com.gym.utils.JsonUtils;
import com.gym.utils.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(TrainerController.class)
class TrainerControllerTest {
    private MockedStatic<StringUtils> mockStringUtil;
    private final Integer passwordLength = 10;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ITrainerService service;

    @BeforeEach
    public void registerStaticMock() {
        mockStringUtil = Mockito.mockStatic(StringUtils.class);
        mockStringUtil.when(() -> StringUtils.generateRandomString(passwordLength))
                .thenReturn("1234567890");
    }

    @AfterEach
    public void unregisterStaticMock() {
        mockStringUtil.close();
    }

    @Test
    void getTrainerProfileTest() {
        TrainerModel trainerModel = JsonUtils.parseResource("trainerRestTest.json", new TypeReference<>() {
        });

        try {
            given(service.getTrainerProfile(trainerModel.getUserName())).willReturn(trainerModel);
            given(service.getAssignedTraineeList(trainerModel.getUserName())).willReturn(List.of());
            mvc.perform(get("/trainers/Kerry.King")
                            .header("password", "1234567890"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").value("Kerry"))
                    .andExpect(jsonPath("$.lastName").value("King"))
                    .andExpect(jsonPath("$.specialization").value("YOGA"))
                    .andExpect(jsonPath("$.traineeList").isArray())
                    .andExpect(jsonPath("$.traineeList.length()").value(0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getTrainerProfileIfNotExistTest() {
        try {
            given(service.getTrainerProfile("Kerry.King1")).willThrow(new IncorrectCredentialException("User name or password incorrect"));
            given(service.getAssignedTraineeList("Kerry.King1")).willThrow(new IncorrectCredentialException("User name or password incorrect"));
            mvc.perform(get("/trainers/Kerry.King1")
                            .header("password", "1234567890"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errors").isNotEmpty())
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors[0]").value("User name or password incorrect"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createTrainerTest() {
        TrainerModel trainerModel = JsonUtils.parseResource("trainerCreateRestTest.json", new TypeReference<>() {
        });
        TrainerCreateDTO trainerCreateDTO = TrainerCreateDTO.builder()
                .firstName("Tom")
                .lastName("Cruze")
                .specialization("YOGA")
                .build();
        given(service.createTrainer("Tom", "Cruze", "YOGA")).willReturn(trainerModel);
        try {
            mvc.perform(post("/trainers").contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.convertObjectToJson(trainerCreateDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.userName").value("Tom.Cruze"))
                    .andExpect(jsonPath("$.password").value("1234567890"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateTrainerTest(){
        TrainerModel trainerModel = JsonUtils.parseResource("trainerRestTest.json", new TypeReference<>() {
        });
        TrainerModel trainerUpdatedModel = JsonUtils.parseResource("trainerUpdatedRestTest.json", new TypeReference<>() {
        });
        TrainerUpdateDTO trainerUpdateDTO = TrainerUpdateDTO.builder()
                .userName("Kerry.King")
                .firstName("Patrick")
                .lastName("Mean")
                .specialization("ZUMBA")
                .isActive(true)
                .build();
        try {
            given(service.getTrainerProfile(trainerModel.getUserName()))
                    .willReturn(trainerModel);
            given(service.updateTrainerProfile(trainerModel.getUserName(), trainerUpdatedModel))
                    .willReturn(trainerUpdatedModel);
            given(service.getAssignedTraineeList(trainerModel.getUserName())).willReturn(List.of());
            mvc.perform(put("/trainers").contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.convertObjectToJson(trainerUpdateDTO))
                            .header("password", "1234567890"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userName").value("Kerry.King"))
                    .andExpect(jsonPath("$.firstName").value("Patrick"))
                    .andExpect(jsonPath("$.lastName").value("Mean"))
                    .andExpect(jsonPath("$.specialization").value("ZUMBA"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}