package com.gym.model;

import lombok.Data;

@Data
public class User {
    long id;
    String firstName;
    String lastName;
    String userName;
    String password;
    boolean isActive;
}
