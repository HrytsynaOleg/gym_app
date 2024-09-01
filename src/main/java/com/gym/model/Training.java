package com.gym.model;

import lombok.Data;

@Data
public class Training {
    long id;
    long traineeId;
    long trainerId;
    String trainingName;
    TrainingType trainingType;
    String trainingDate;
    int duration;
}