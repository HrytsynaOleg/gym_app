package com.gym.dto.training;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingCreateDTO {
    @NotBlank(message = "Trainee username name must be not empty")
    private String traineeUserName;
    @NotBlank(message = "Trainer username must be not empty")
    private String trainerUserName;
    @NotBlank(message = "Training name must be not empty")
    private String trainingName;
    @NotBlank(message = "Training date must be not empty")
    private String trainingDate;
    @NotBlank(message = "Training duration must be not empty")
    private String trainingDuration;
}
