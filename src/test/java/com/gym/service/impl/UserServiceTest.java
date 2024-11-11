package com.gym.service.impl;

import com.gym.exception.IncorrectCredentialException;
import com.gym.model.UserModel;
import com.gym.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    private final IUserService userService;

    @Autowired
    public UserServiceTest(IUserService userService) {
        this.userService = userService;
    }

    @Test
    void updateTrainerPasswordTest() {
        UserModel updatedUser;
        try {
            userService.changePassword("Kerry.King", "123");
            updatedUser = userService.getUserProfile("Kerry.King");
            userService.changePassword("Kerry.King", "1234567890");
        } catch (IncorrectCredentialException e) {
            throw new RuntimeException(e);
        }
    }
}
