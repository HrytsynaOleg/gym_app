package com.gym.dto.trainee;

import com.gym.dto.trainer.TrainerListItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraineeUpdatedProfileDTO {
    private String userName;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private boolean isActive;
    private List<TrainerListItemDTO> trainerList;

}
