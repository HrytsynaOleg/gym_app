package com.gym.utils;

import com.gym.dto.TraineeDTO;
import com.gym.dto.TrainerProfileDTO;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;

public class DTOMapper {
    public static TrainerProfileDTO mapTrainerModelToTrainerProfileDTO(TrainerModel model){
        return TrainerProfileDTO.builder()
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .isActive(model.getIsActive())
                .specialization(model.getTrainingType().toString())
                .build();
    }

    public static TraineeDTO mapTraineeModelToTraineeDTO(TraineeModel model) {
        return TraineeDTO.builder()
                .userName(model.getUserName())
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .build();
    }
}

