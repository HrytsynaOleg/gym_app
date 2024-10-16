package com.gym.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraineeUpdateDTO {
    @NotBlank(message = "User name must be not empty")
    private String userName;
    @NotBlank(message = "First name must be not empty")
    private String firstName;
    @NotBlank(message = "Last name must be not empty")
    private String lastName;
    private String dateOfBirth;
    private String address;
    @NotNull(message = "Activated status must be not null")
    private boolean isActive;
}
