package com.gym.dto.training;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingTypeListItemDTO {
    private String trainingTypeName;
    private int trainingTypeId;
}
