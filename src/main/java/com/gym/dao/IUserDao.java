package com.gym.dao;

import com.gym.entity.User;
import com.gym.model.UserCredentials;

public interface IUserDao {
    User getByName(String name);
    UserCredentials getUserCredentials(String name);
    long getUserCount(String firstName, String lastName);
}
