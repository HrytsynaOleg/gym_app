package com.gym.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dto.trainee.TraineeCreateDTO;
import com.gym.dto.trainee.TraineeUpdateDTO;
import com.gym.dto.trainee.TraineeUpdateTrainerListDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.model.UserModel;
import com.gym.security.JwtTokenService;
import com.gym.service.ITraineeService;
import com.gym.service.impl.UserService;
import com.gym.utils.JsonUtils;
import com.gym.utils.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TraineeControllerTest {
    private MockedStatic<StringUtils> mockStringUtil;
    private final Integer passwordLength = 10;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ITraineeService service;

    @MockBean
    private JwtTokenService tokenService;

    @MockBean
    private UserService userService;

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
    @WithMockUser(value = "Bruce.Dickinson")
    void getTraineeProfileTest() {
        TraineeModel traineeModel = JsonUtils.parseResource("traineeRestTest.json", new TypeReference<>() {
        });

        try {
            given(service.getTraineeProfile("Bruce.Dickinson")).willReturn(traineeModel);
            given(service.getAssignedTrainerList("Bruce.Dickinson")).willReturn(List.of());
            mvc.perform(get("/trainees/Bruce.Dickinson"))
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
    @WithMockUser(value = "Kerry.King")
    void getTraineeProfileIfUserNameNotMatchTest() {
        try {
            mvc.perform(get("/trainees/Bruce.Dickinson"))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.errors").isNotEmpty())
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors[0]").value("Access for user Kerry.King denied"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithAnonymousUser
    void getTraineeProfileIfUserUnauthorizedTest() {
        try {
            mvc.perform(get("/trainees/Bruce.Dickinson"))
                    .andExpect(status().isForbidden());
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
        UserModel userModel = UserModel.builder()
                .userName("Kerry.King")
                .token("test token")
                .build();
        given(service.createTrainee("Tom", "Cruze", "New York", "1968-03-12"))
                .willReturn(traineeModel);
        given(tokenService.getUserName("test token")).willReturn("Kerry.King");
        try {
            given(userService.getUserProfile("Kerry.King")).willReturn(userModel);
        } catch (IncorrectCredentialException e) {
            throw new RuntimeException(e);
        }
        try {
            mvc.perform(post("/trainees")
                            .header("Authorization", "Bearer test token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.convertObjectToJson(traineeCreateDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.userName").value("Tom.Cruze"))
                    .andExpect(jsonPath("$.password").value("1234567890"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(value = "Bruce.Dickinson")
    void updateTraineeTest() {
        TraineeModel traineeModel = JsonUtils.parseResource("traineeRestTest.json", new TypeReference<>() {
        });
        TraineeModel traineeUpdatedModel = JsonUtils.parseResource("traineeUpdatedRestTest.json", new TypeReference<>() {
        });
        TraineeUpdateDTO traineeUpdateDTO = TraineeUpdateDTO.builder()
                .userName("Bruce.Dickinson")
                .firstName("Neil")
                .lastName("Young")
                .dateOfBirth("1982-09-15")
                .address("Boston")
                .isActive(true)
                .build();
        try {
            given(service.getTraineeProfile(traineeModel.getUserName()))
                    .willReturn(traineeModel);
            given(service.update(traineeModel.getUserName(), traineeUpdatedModel))
                    .willReturn(traineeUpdatedModel);
            given(service.getAssignedTrainerList("Bruce.Dickinson")).willReturn(List.of());
            mvc.perform(put("/trainees/Bruce.Dickinson/update").contentType(MediaType.APPLICATION_JSON)
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
    @WithMockUser(value = "Bruce.Dickinson")
    void deleteTraineeTest() {
        TraineeModel traineeModel = JsonUtils.parseResource("traineeRestTest.json", new TypeReference<>() {
        });
        try {
            doNothing().when(service).delete(traineeModel.getUserName());
            mvc.perform(delete("/trainees/Bruce.Dickinson/delete")
                            .header("password", "1234567890"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(value = "Bruce.Dickinson")
    void getNotAssignedTrainerListTest() {
        TraineeModel traineeModel = JsonUtils.parseResource("traineeRestTest.json", new TypeReference<>() {
        });
        TrainerModel trainerModel = JsonUtils.parseResource("trainerRestTest.json", new TypeReference<>() {
        });
        try {
            given(service.getNotAssignedTrainerList(traineeModel.getUserName()))
                    .willReturn(List.of(trainerModel));
            mvc.perform(get("/trainees/Bruce.Dickinson/not-assigned-trainers")
                            .header("password", "1234567890"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.trainers.length()").value(1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(value = "Bruce.Dickinson")
    void updateTrainerListTest() {
        TraineeModel traineeModel = JsonUtils.parseResource("traineeRestTest.json", new TypeReference<>() {
        });
        List<TrainerModel> updatedTrainerModelList = JsonUtils.parseResource("traineeUpdateTrainerList.json",
                new TypeReference<>() {
                });
        List<String> newTrainerList = List.of("Kerry.King", "Tom.Arraya");

        TraineeUpdateTrainerListDTO traineeUpdateTrainerListDTO = TraineeUpdateTrainerListDTO.builder()
                .userName("Bruce.Dickinson")
                .trainerList(List.of("Kerry.King", "Tom.Arraya"))
                .build();
        try {
            given(service.updateTrainerList(traineeModel.getUserName(), newTrainerList))
                    .willReturn(updatedTrainerModelList);
            mvc.perform(put("/trainees/Bruce.Dickinson/trainers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.convertObjectToJson(traineeUpdateTrainerListDTO))
                            .header("password", "1234567890"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.trainers.length()").value(2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}