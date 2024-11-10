package com.gym.dto.trainer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties
public class TrainerUpdateDTO {
    @NotBlank(message = "User name must be not empty")
    private String userName;
    @NotBlank(message = "First name must be not empty")
    private String firstName;
    @NotBlank(message = "Last name must be not empty")
    private String lastName;
    @NotBlank(message = "Specialization must be not empty")
    private String specialization;
    @NotNull(message = "Activated status must be not null")
    private boolean isActive;

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }
}
