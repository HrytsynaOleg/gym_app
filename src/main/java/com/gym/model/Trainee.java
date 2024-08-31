package com.gym.model;

import lombok.Data;

@Data
public class Trainee {
    long id;
    User user;
    String address;
    String dateOfBirth;
}
