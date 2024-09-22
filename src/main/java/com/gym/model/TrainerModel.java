package com.gym.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Jacksonized
public class TrainerModel extends UserModel {
    long id;
    @NotNull
    TrainingTypeEnum trainingType;
}
