package com.gym.actuator;

import com.gym.service.impl.UserCredentialsService;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CredentialsServiceHealthIndicator implements HealthIndicator {
    private final UserCredentialsService service;

    public CredentialsServiceHealthIndicator(UserCredentialsService service) {
        this.service = service;
    }

    @Override
    public Health health() {
            if (service!=null) {
                return Health.up().withDetail("Credential service", "Available").build();
            } else {
                return Health.down().withDetail("Credential service", "Unavailable").build();
            }
    }
}
