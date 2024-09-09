package com.gym.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class Trainee extends User{
    long id;
    String address;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;
}
