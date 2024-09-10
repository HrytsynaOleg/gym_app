package com.gym.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Data
@Builder
@Jacksonized
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