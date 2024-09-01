package com.gym.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Trainee extends User{
    long id;
    String address;
    String dateOfBirth;
}
