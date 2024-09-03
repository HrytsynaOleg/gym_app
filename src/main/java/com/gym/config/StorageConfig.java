package com.gym.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.model.Trainee;
import com.gym.model.Trainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import utils.StorageUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

@Configuration
public class StorageConfig {

    private Map<String, Trainer> trainerStorage;
    private Map<String, Trainee> traineeStorage;
    @Value("${file.trainers}")
    private String trainerStorageFilePath;
    @Value("${file.trainees}")
    private String traineeStorageFilePath;

    @Bean
    public  Map<String,Trainer> trainerStorage(){
        return trainerStorage;
    }

    @Bean
    public  Map<String,Trainee> traineeStorage(){
        return traineeStorage;
    }

    @PostConstruct
    void initStorage(){
        trainerStorage = StorageUtils.buildMapFromFile(trainerStorageFilePath, new TypeReference<>() {
        });
        traineeStorage = StorageUtils.buildMapFromFile(traineeStorageFilePath, new TypeReference<>() {
        });
    }
}
