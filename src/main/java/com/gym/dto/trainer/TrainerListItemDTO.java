package com.gym.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainerListItemDTO {
    private String userName;
    private String firstName;
    private String lastName;
    private String specialization;
}
