package com.gym.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dto.trainee.TraineeCreateDTO;
import com.gym.dto.trainee.TraineeUpdateDTO;
import com.gym.dto.trainee.TraineeUpdateTrainerListDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.model.UserCredentials;
import com.gym.service.ITraineeService;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                    .andExpect(jsonPath("$.firstName").value("Neil"))
                    .andExpect(jsonPath("$.lastName").value("Young"))
                    .andExpect(jsonPath("$.dateOfBirth").value("1982-09-15"))
                    .andExpect(jsonPath("$.address").value("Boston"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deleteTraineeTest(){
        TraineeModel traineeModel = JsonUtils.parseResource("traineeRestTest.json", new TypeReference<>() {
        });
        UserCredentials credentials = UserCredentials.builder()
                .userName(traineeModel.getUserName())
                .password(traineeModel.getPassword())
                .build();
        try {
            doNothing().when(service).delete(credentials);
            mvc.perform(delete("/trainee/Bruce.Dickinson")
                            .header("password", "1234567890"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getNotAssignedTrainerListTest(){
        TraineeModel traineeModel = JsonUtils.parseResource("traineeRestTest.json", new TypeReference<>() {
        });
        TrainerModel trainerModel = JsonUtils.parseResource("trainerRestTest.json", new TypeReference<>() {
        });
        UserCredentials credentials = UserCredentials.builder()
                .userName(traineeModel.getUserName())
                .password(traineeModel.getPassword())
                .build();
        try {
            given(service.getNotAssignedTrainerList(credentials))
                    .willReturn(List.of(trainerModel));
            mvc.perform(get("/trainee/Bruce.Dickinson/not-assigned-trainers")
                            .header("password", "1234567890"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.trainers.length()").value(1));
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateTrainerListTest(){
        TraineeModel traineeModel = JsonUtils.parseResource("traineeRestTest.json", new TypeReference<>() {
        });
        List<TrainerModel> updatedTrainerModelList = JsonUtils.parseResource("traineeUpdateTrainerList.json",
                new TypeReference<>() {
        });
        UserCredentials credentials = UserCredentials.builder()
                .userName(traineeModel.getUserName())
                .password(traineeModel.getPassword())
                .build();
        List<String> newTrainerList = List.of("Kerry.King", "Tom.Arraya");

        TraineeUpdateTrainerListDTO traineeUpdateTrainerListDTO = TraineeUpdateTrainerListDTO.builder()
                .userName("Bruce.Dickinson")
                .trainerList(List.of("Kerry.King", "Tom.Arraya"))
                .build();
        try {
            given(service.updateTrainerList(credentials, newTrainerList))
                    .willReturn(updatedTrainerModelList);
            mvc.perform(put("/trainee/Bruce.Dickinson/trainers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.convertObjectToJson(traineeUpdateTrainerListDTO))
                            .header("password", "1234567890"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.trainers.length()").value(2));
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}