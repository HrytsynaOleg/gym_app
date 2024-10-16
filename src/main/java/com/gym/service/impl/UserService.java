package com.gym.service.impl;


import com.gym.dao.IUserDao;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.UserCredentials;
import com.gym.model.UserModel;
import com.gym.service.IModelValidator;
import com.gym.service.IUserCredentialsService;
import com.gym.service.IUserService;
import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service("userService")
public class UserService implements IUserService {
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IUserCredentialsService credentialsService;
    @Autowired
    private IModelValidator validator;

    @Override
    public UserModel getUserProfile(UserCredentials credentials) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        return userDao.getUserByName(credentials.getUserName());
    }

    @Override
    public void login(UserCredentials credentials) throws IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
    }

    @Override
    public void changePassword(UserCredentials credentials, String newPassword) throws ValidationException,
            IncorrectCredentialException {
        credentialsService.verifyCredentials(credentials);
        UserModel user = userDao.getUserByName(credentials.getUserName());
        user.setPassword(newPassword);
        validator.validate(user);
        userDao.update(user);
        log.info("Password changed. Transaction Id {}", MDC.get("transactionId"));
    }
}
