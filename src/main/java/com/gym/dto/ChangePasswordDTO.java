package com.gym.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordDTO {
    @NotBlank(message = "Username must be not empty")
    String userName;
    @NotBlank(message = "Old password must be not empty")
    String oldPassword;
    @NotBlank(message = "New password must be not empty")
    String newPassword;
}
