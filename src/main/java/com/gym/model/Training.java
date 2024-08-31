package com.gym.model;

import lombok.Data;

import java.util.Date;

@Data
public class Training {
    long id;
    Trainee trainee;
    Trainer trainer;
    String trainingName;
    TrainingType trainingType;
    Date trainingDate;
    long duration;
}