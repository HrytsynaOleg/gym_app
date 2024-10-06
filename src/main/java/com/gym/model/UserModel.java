package com.gym.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@SuperBuilder
public class UserModel {
    long userId;
    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
    @NotBlank
    String userName;
    @NotBlank
    String password;
    @NotNull
    boolean isActive;

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
