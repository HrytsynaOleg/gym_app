package com.gym.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dto.TraineeCreateDTO;
import com.gym.dto.TraineeUpdateDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.TraineeModel;
import com.gym.model.UserCredentials;
import com.gym.service.ITraineeService;
import com.gym.utils.JsonUtils;
import com.gym.utils.StringUtils;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TraineeController.class)
class TraineeControllerTest {
    private MockedStatic<StringUtils> mockStringUtil;
    private final Integer passwordLength = 10;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ITraineeService service;

    @Test
    void getTraineeProfileTest() {
        TraineeModel traineeModel = JsonUtils.parseResource("traineeRestTest.json", new TypeReference<>() {
        });
        UserCredentials credentials = UserCredentials.builder()
                .userName(traineeModel.getUserName())
                .password(traineeModel.getPassword())
                .build();

        try {
            given(service.getTraineeProfile(credentials)).willReturn(traineeModel);
            given(service.getAssignedTrainerList(credentials)).willReturn(List.of());
            mvc.perform(get("/trainee/Bruce.Dickinson")
                            .header("password", "1234567890"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").value("Bruce"))
                    .andExpect(jsonPath("$.lastName").value("Dickinson"))
                    .andExpect(jsonPath("$.dateOfBirth").value("1972-08-11"))
                    .andExpect(jsonPath("$.address").value("Los Angeles"))
                    .andExpect(jsonPath("$.trainerList").isArray())
                    .andExpect(jsonPath("$.trainerList.length()").value(0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getTraineeProfileIfNotExistTest() {
        UserCredentials credentials = UserCredentials.builder()
                .userName("Bruce.Dickinson1")
                .password("1234567890")
                .build();
        try {
            given(service.getTraineeProfile(credentials)).willThrow(new IncorrectCredentialException("User name or password incorrect"));
            given(service.getAssignedTrainerList(credentials)).willThrow(new IncorrectCredentialException("User name or password incorrect"));
            mvc.perform(get("/trainee/Bruce.Dickinson1")
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
    void createTraineeTest() {
        TraineeModel traineeModel = JsonUtils.parseResource("traineeCreateRestTest.json", new TypeReference<>() {
        });
        mockStringUtil = Mockito.mockStatic(StringUtils.class);
        mockStringUtil.when(() -> StringUtils.generateRandomString(passwordLength))
                .thenReturn("1234567890");
        TraineeCreateDTO traineeCreateDTO = TraineeCreateDTO.builder()
                .firstName("Tom")
                .lastName("Cruze")
                .dateOfBirth("1968-03-12")
                .address("New York")
                .build();
        given(service.createTrainee("Tom", "Cruze", "New York", "1968-03-12"))
                .willReturn(traineeModel);
        try {
            mvc.perform(post("/trainee").contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.convertObjectToJson(traineeCreateDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.userName").value("Tom.Cruze"))
                    .andExpect(jsonPath("$.password").value("1234567890"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            mockStringUtil.close();
        }
    }

    @Test
    void updateTraineeTest(){
        TraineeModel traineeModel = JsonUtils.parseResource("traineeRestTest.json", new TypeReference<>() {
        });
        TraineeModel traineeUpdatedModel = JsonUtils.parseResource("traineeUpdatedRestTest.json", new TypeReference<>() {
        });
        UserCredentials credentials = UserCredentials.builder()
                .userName(traineeModel.getUserName())
                .password(traineeModel.getPassword())
                .build();
        mockStringUtil = Mockito.mockStatic(StringUtils.class);
        mockStringUtil.when(() -> StringUtils.generateRandomString(passwordLength))
                .thenReturn("1234567890");
        TraineeUpdateDTO traineeUpdateDTO = TraineeUpdateDTO.builder()
                .userName("Bruce.Dickinson")
                .firstName("Neil")
                .lastName("Young")
                .dateOfBirth("1982-09-15")
                .address("Boston")
                .isActive(true)
                .build();
        try {
            given(service.getTraineeProfile(credentials))
                    .willReturn(traineeModel);
            given(service.update(credentials, traineeUpdatedModel))
                    .willReturn(traineeUpdatedModel);
            given(service.getAssignedTrainerList(credentials)).willReturn(List.of());
            mvc.perform(put("/trainee").contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.convertObjectToJson(traineeUpdateDTO))
                            .header("password", "1234567890"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userName").value("Bruce.Dickinson"))
                    .andExpect(jsonPath("$.firstName").value("Neil"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            mockStringUtil.close();
        }
    }
}