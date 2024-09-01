package com.gym.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Trainer extends User{
    long id;
    TrainingType trainingType;
}
