package com.gym.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.gym.utils.StorageUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "com.gym")
@PropertySource("classpath:application.properties")
@Log4j2
public class StorageConfig {

    private Map<String, TrainerModel> trainerStorage;
    private Map<String, TraineeModel> traineeStorage;
    private Map<String, TrainingModel> trainingStorage;
    @Value("${file.trainers}")
    private String trainerStorageFilePath;
    @Value("${file.trainees}")
    private String traineeStorageFilePath;
    @Value("${file.training}")
    private String trainingStorageFilePath;

    @Bean(name = "trainerStorage")
    public Map<String, TrainerModel> trainerStorage() {
        return trainerStorage;
    }

    @Bean(name = "traineeStorage")
    public Map<String, TraineeModel> traineeStorage() {
        return traineeStorage;
    }

    @Bean(name = "trainingStorage")
    public Map<String, TrainingModel> trainingStorage() {
        return trainingStorage;
    }

    @Bean
    public EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("com.gym");
    }

    @PostConstruct
    void initStorage() {
        log.info("Start storage initialization");
        trainerStorage = StorageUtils.buildMapFromFile(trainerStorageFilePath, new TypeReference<>() {
        });
        traineeStorage = StorageUtils.buildMapFromFile(traineeStorageFilePath, new TypeReference<>() {
        });
        trainingStorage = StorageUtils.buildMapFromFile(trainingStorageFilePath, new TypeReference<>() {
        });
        log.info("Finish storage initialization");
    }
}
