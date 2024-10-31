package com.gym.actuator;

import com.gym.service.impl.UserService;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class UserServiceHealthIndicator implements HealthIndicator {

    private final UserService service;

    public UserServiceHealthIndicator(UserService service) {
        this.service = service;
    }

    @Override
    public Health health() {
        if (service!=null) {
            return Health.up().withDetail("User service", "Available").build();
        } else {
            return Health.down().withDetail("User service", "Unavailable").build();
        }
    }
}
