package com.gym.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class Trainee extends User{
    long id;
    String address;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;
}
