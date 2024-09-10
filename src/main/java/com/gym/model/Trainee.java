package com.gym.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Jacksonized
public class Trainee extends User{
    long id;
    String address;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;
}
