package com.gym.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCredentials {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
}
