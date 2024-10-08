package com.gym.service.impl;

import com.gym.dao.IUserDao;
import com.gym.entity.User;
import com.gym.exceptions.IncorrectCredentialException;
import com.gym.model.UserCredentials;
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
        User user = dao.getByName(credentials.getUserName());
        if (user == null || !user.getPassword().equals(credentials.getPassword())) {
            throw new IncorrectCredentialException("User name or password incorrect");
        }
    }
}
