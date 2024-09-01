package com.gym.config;

import com.gym.model.Trainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import utils.MapUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

@Configuration
public class StorageConfig {

    private Map<String, Trainer> trainerStorage;
    @Value("${file.trainers}")
    private String trainerStorageFilePath;

    @Bean
    public  Map<String,Trainer> trainerStorage(){
        return trainerStorage;
    }

    @PostConstruct
    void initStorage(){
        trainerStorage = MapUtils.buildMapFromFile(trainerStorageFilePath);
    }
}
