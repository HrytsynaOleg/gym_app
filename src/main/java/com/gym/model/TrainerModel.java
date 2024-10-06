package com.gym.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Jacksonized
public class TrainerModel extends UserModel {
    long id;
    @NotNull
    TrainingTypeEnum trainingType;
}
