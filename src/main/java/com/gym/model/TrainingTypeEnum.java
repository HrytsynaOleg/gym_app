package com.gym.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TrainingTypeEnum {
    @JsonProperty("FITNESS")
    FITNESS(9, "Fitness"),
    @JsonProperty("YOGA")
    YOGA(10, "Yoga"),
    @JsonProperty("ZUMBA")
    ZUMBA(11, "Zumba"),
    @JsonProperty("STRETCHING")
    STRETCHING(12, "Stretching"),
    @JsonProperty("RESISTANCE")
    RESISTANCE(14, "Resistance");

    private final long id;
    private final String name;

    TrainingTypeEnum(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TrainingTypeEnum getTrainingTypeById(long id){
        return Arrays.stream(TrainingTypeEnum.values())
                .filter(i -> id == i.getId())
                .findFirst()
                .orElseThrow();
    }
}
