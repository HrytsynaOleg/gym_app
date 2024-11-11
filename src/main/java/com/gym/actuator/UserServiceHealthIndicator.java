package com.gym.actuator;

import com.gym.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class UserServiceHealthIndicator implements HealthIndicator {

    @Autowired
    private UserService service;

    @Override
    public Health health() {
        if (service != null) {
            return Health.up().withDetail("User service", "Available").build();
        } else {
            return Health.down().withDetail("User service", "Unavailable").build();
        }
    }
}
