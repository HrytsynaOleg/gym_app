package com.gym.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Training {
    long id;
    long traineeId;
    long trainerId;
    String trainingName;
    TrainingType trainingType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date trainingDate;
    int duration;
}