package com.gym.controller;

import com.gym.exception.IncorrectCredentialException;
import com.gym.model.UserCredentials;
import com.gym.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IUserService service;

    @Test
    void loginTest() {
        UserCredentials credentials = UserCredentials.builder()
                .userName("Kerry.King1")
                .password("1234567890")
                .build();
        try {
            doNothing().when(service).login(credentials);
            mvc.perform(get("/user/Kerry.King")
                            .header("password", "1234567890"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void loginUnauthorizedUserTest() {
        UserCredentials credentials = UserCredentials.builder()
                .userName("Kerry.King1")
                .password("1234567890")
                .build();
        try {

            doThrow(new IncorrectCredentialException("User name or password incorrect")).when(service).login(credentials);
            mvc.perform(get("/user/Kerry.King1")
                            .header("password", "1234567890"))
                    .andExpect(status().isUnauthorized());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}