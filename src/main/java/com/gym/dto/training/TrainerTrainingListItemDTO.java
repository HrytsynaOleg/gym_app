package com.gym.dto.training;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainerTrainingListItemDTO {
    private String trainingName;
    private String trainingDate;
    private String trainingType;
    private String trainingDuration;
    private String traineeName;
}