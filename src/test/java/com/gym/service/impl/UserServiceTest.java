package com.gym.service.impl;

import com.gym.exception.IncorrectCredentialException;
import com.gym.model.UserCredentials;
import com.gym.model.UserModel;
import com.gym.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserServiceTest {
    private final IUserService userService;

    @Autowired
    public UserServiceTest(IUserService userService) {
        this.userService = userService;
    }

    @Test
    void updateTrainerPasswordTest() {
        UserCredentials credentials = UserCredentials.builder()
                .userName("Kerry.King")
                .password("1234567890")
                .build();
        UserModel updatedUser;
        try {
            userService.changePassword("Kerry.King", "123");
            credentials.setPassword("123");
            updatedUser = userService.getUserProfile("Kerry.King");
            userService.changePassword("Kerry.King", "1234567890");
        } catch (IncorrectCredentialException e) {
            throw new RuntimeException(e);
        }
        assertEquals("123", updatedUser.getPassword());
    }
}
