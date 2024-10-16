package com.gym.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraineeCreateDTO {
    @NotBlank(message = "First name must be not empty")
    private String firstName;
    @NotBlank(message = "Last name must be not empty")
    private String lastName;
    private String dateOfBirth;
    private String address;
}
