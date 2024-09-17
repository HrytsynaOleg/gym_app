package com.gym.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TrainingType {
    FITNESS(9, "Fitness"),
    YOGA(10, "Yoga"),
    ZUMBA(11, "Zumba"),
    STRETCHING(12, "Stretching"),
    RESISTANCE(14, "Resistance");

    private final long id;
    private final String name;

    TrainingType(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TrainingType getTrainingTypeById(long id){
        return Arrays.stream(TrainingType.values())
                .filter(i -> id == i.getId())
                .findFirst()
                .orElseThrow();
    }
}
