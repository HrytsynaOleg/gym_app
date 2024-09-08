package com.gym.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.model.Trainee;
import com.gym.model.Trainer;
import com.gym.model.Training;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import utils.StorageUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "com.gym")
@PropertySource("classpath:application.properties")
public class StorageConfig {

    private Map<String, Trainer> trainerStorage;
    private Map<String, Trainee> traineeStorage;
    private Map<String, Training> trainingStorage;
    @Value("${file.trainers}")
    private String trainerStorageFilePath;
    @Value("${file.trainees}")
    private String traineeStorageFilePath;
    @Value("${file.training}")
    private String trainingStorageFilePath;

    @Bean(name="trainerStorage")
    public  Map<String,Trainer> trainerStorage(){
        return trainerStorage;
    }

    @Bean(name="traineeStorage")
    public  Map<String,Trainee> traineeStorage(){
        return traineeStorage;
    }

    @Bean(name="trainingStorage")
    public  Map<String,Training> trainingStorage(){
        return trainingStorage;
    }

    @PostConstruct
    void initStorage(){
        trainerStorage = StorageUtils.buildMapFromFile(trainerStorageFilePath, new TypeReference<>() {
        });
        traineeStorage = StorageUtils.buildMapFromFile(traineeStorageFilePath, new TypeReference<>() {
        });
        trainingStorage = StorageUtils.buildMapFromFile(trainingStorageFilePath, new TypeReference<>() {
        });
    }
}
