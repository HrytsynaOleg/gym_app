package com.gym.model;

import lombok.Data;

@Data
public abstract class User {
    String firstName;
    String lastName;
    String userName;
    String password;
    boolean isActive;

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
