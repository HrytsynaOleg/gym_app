package com.gym.service.impl;

import com.gym.dao.IUserDao;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.UserCredentials;
import com.gym.model.UserModel;
import com.gym.service.IUserCredentialsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("credentialsService")
@Log4j2
public class UserCredentialsService implements IUserCredentialsService {
    @Autowired
    private IUserDao dao;
    @Override
    public void verifyCredentials(UserCredentials credentials) throws IncorrectCredentialException {
        UserModel user = dao.getUserByName(credentials.getUserName());
        if (user == null || !user.getPassword().equals(credentials.getPassword())) {
            throw new IncorrectCredentialException("User not found");
        }
    }
}
