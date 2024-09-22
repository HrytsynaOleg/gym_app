package com.gym.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCredentials {
    private String userName;
    private String password;
}
