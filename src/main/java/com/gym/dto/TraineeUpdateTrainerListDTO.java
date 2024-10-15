package com.gym.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraineeUpdateTrainerListDTO {
    @NotBlank(message = "User name must be not empty")
    private String userName;
    @NotNull(message = "Trainer list must be not null")
    private List<String> trainerList;
}
