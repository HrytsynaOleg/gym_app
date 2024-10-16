package com.gym.dao;

import com.gym.model.UserCredentials;
import com.gym.model.UserModel;

public interface IUserDao {
    UserModel getUserByName(String name);
    UserCredentials getUserCredentials(String name);
    long getUserCount(String firstName, String lastName);
    void update(UserModel user);
}
