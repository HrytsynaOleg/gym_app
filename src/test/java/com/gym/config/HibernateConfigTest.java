package com.gym.config;

import com.gym.dao.impl.TrainerJdbcDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class HibernateConfigTest {
    private static ApplicationContext applicationContext;

    @BeforeAll
    public static void init() {
        applicationContext = new AnnotationConfigApplicationContext(HibernateConfig.class);
    }

    @Test
    void databaseDirectConnectionTest(){
        String url = "jdbc:mysql://localhost:3306/gym?serverTimezone=Europe/Kyiv&verifyServerCertificate=false&useSSL=false";
        String username = "root";
        String password = "123456";
        System.out.println("Connecting...");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    @Test
    void databaseHibernateConfigConnectionTest(){
        DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    @Test
    void trainerDaoJdbcBeanTest(){
        TrainerJdbcDao trainerDao = applicationContext.getBean(TrainerJdbcDao.class);
        assertNotNull(trainerDao);
        assertEquals(TrainerJdbcDao.class, trainerDao.getClass());
    }
}