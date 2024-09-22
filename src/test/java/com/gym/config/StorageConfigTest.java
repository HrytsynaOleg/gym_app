package com.gym.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

class StorageConfigTest {
    private static ApplicationContext applicationContext;

    @BeforeAll
    public static void init() {
        applicationContext = new AnnotationConfigApplicationContext(StorageConfig.class);
    }

    @Test
    void entityManagerFactoryTest(){
        EntityManagerFactory factory = applicationContext.getBean(EntityManagerFactory.class);
        assertNotNull(factory);
    }

    @Test
    void databaseConnection(){


    }
}