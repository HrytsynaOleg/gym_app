package com.gym.model;

import lombok.Data;

@Data
public class Trainer {
    long id;
    User user;
    TrainingType trainingType;
}
