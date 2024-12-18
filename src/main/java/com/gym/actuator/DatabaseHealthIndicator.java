package com.gym.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1000)) {
                DatabaseMetaData metaData = connection.getMetaData();
                return Health.up()
                        .withDetail("Database", "Available")
                        .withDetail("Database name", metaData.getDatabaseProductName())
                        .withDetail("Database url", metaData.getURL())
                        .build();
            } else {
                return Health.down().withDetail("Database", "Unavailable").build();
            }
        } catch (SQLException e) {
            return Health.down(e).withDetail("Database", "Error").build();
        }
    }
}
