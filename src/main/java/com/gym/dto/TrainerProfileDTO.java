package com.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainerProfileDTO {
    private String firstName;
    private String lastName;
    private String specialization;
    private boolean isActive;
    private List<TraineeDTO> traineeList;

}
