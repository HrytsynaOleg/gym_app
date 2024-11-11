package com.gym.controller;

import com.gym.dto.ChangePasswordDTO;
import com.gym.service.impl.UserService;
import com.gym.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    @Test
    @WithMockUser(value = "Kerry.King")
    void changePasswordTest(){
        doNothing().when(service).changePassword("Kerry.King", "123");
        ChangePasswordDTO changePasswordDTO = ChangePasswordDTO.builder()
                .userName("Kerry.King")
                .newPassword("123")
                .build();
        try {
            mvc.perform(put("/users/Kerry.King").contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.convertObjectToJson(changePasswordDTO)))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}