package com.gym.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerCreateDTO {
    @NotBlank(message = "First name must be not empty")
    private String firstName;
    @NotBlank(message = "Last name must be not empty")
    private String lastName;
    @NotBlank(message = "Specialization must be not empty")
    private String specialization;
}
