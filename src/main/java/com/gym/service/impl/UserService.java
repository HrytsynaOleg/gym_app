package com.gym.service.impl;


import com.gym.dao.IUserDao;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.UserCredentials;
import com.gym.model.UserModel;
import com.gym.service.IModelValidator;
import com.gym.service.IUserCredentialsService;
import com.gym.service.IUserService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Counter;

import java.util.NoSuchElementException;

@Log4j2
@Service("userService")
public class UserService implements IUserService {
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IUserCredentialsService credentialsService;
    @Autowired
    private IModelValidator validator;

    private final Counter userLoginCounter;
    private final PasswordEncoder passwordEncoder;

    public UserService(MeterRegistry registry) {
        userLoginCounter = Counter.builder("users.login")
                .description("Number of users login created")
                .register(registry);
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserModel getUserProfile(String username) throws IncorrectCredentialException {
        return userDao.getUserByName(username);
    }

    @Override
    public void login(UserCredentials credentials) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        userLoginCounter.increment();
    }

    @Override
    public void changePassword(String username, String newPassword) throws ValidationException,
            NoSuchElementException {
        UserModel user = userDao.getUserByName(username);
        String password = "{bcrypt}" + passwordEncoder.encode(newPassword);
        user.setPassword(password);
        validator.validate(user);
        userDao.update(user);
        log.info("Password changed. Transaction Id {}", MDC.get("transactionId"));
    }
}
