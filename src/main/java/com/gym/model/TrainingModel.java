package com.gym.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@Builder
@Jacksonized
public class TrainingModel {
    long id;
    @Min(1)
    long traineeId;
    @Min(1)
    long trainerId;
    @NotBlank
    String trainingName;
    @NotNull
    TrainingTypeEnum trainingType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    LocalDate trainingDate;
    @Min(1)
    int duration;
}