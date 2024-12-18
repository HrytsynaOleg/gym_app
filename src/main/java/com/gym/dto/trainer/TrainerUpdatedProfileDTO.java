package com.gym.dto.trainer;

import com.gym.dto.trainee.TraineeListItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainerUpdatedProfileDTO {
    private String userName;
    private String firstName;
    private String lastName;
    private String specialization;
    private boolean isActive;
    private List<TraineeListItemDTO> traineeList;

}
