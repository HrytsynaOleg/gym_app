package com.gym.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Training {
    long id;
    long traineeId;
    long trainerId;
    String trainingName;
    TrainingType trainingType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate trainingDate;
    int duration;
}