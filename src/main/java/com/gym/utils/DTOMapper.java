package com.gym.utils;

import com.gym.dto.trainee.TraineeListItemDTO;
import com.gym.dto.trainee.TraineeProfileDTO;
import com.gym.dto.trainee.TraineeUpdateDTO;
import com.gym.dto.trainee.TraineeUpdatedProfileDTO;
import com.gym.dto.trainer.TrainerListItemDTO;
import com.gym.dto.trainer.TrainerProfileDTO;
import com.gym.dto.trainer.TrainerUpdateDTO;
import com.gym.dto.trainer.TrainerUpdatedProfileDTO;
import com.gym.dto.training.TraineeTrainingListItemDTO;
import com.gym.dto.training.TrainerTrainingListItemDTO;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingModel;
import com.gym.model.TrainingTypeEnum;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DTOMapper {
    private static final String dateFormat = "yyyy-MM-dd";

    public static TrainerProfileDTO mapTrainerModelToTrainerProfileDTO(TrainerModel model) {
        return TrainerProfileDTO.builder()
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .isActive(model.getIsActive())
                .specialization(model.getTrainingType().toString())
                .build();
    }

    public static TraineeListItemDTO mapTraineeModelToTraineeDTO(TraineeModel model) {
        return TraineeListItemDTO.builder()
                .userName(model.getUserName())
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .build();
    }

    public static TraineeProfileDTO mapTraineeModelToTraineeProfileDTO(TraineeModel model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return TraineeProfileDTO.builder()
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .isActive(model.getIsActive())
                .dateOfBirth(model.getDateOfBirth().format(formatter))
                .address(model.getAddress())
                .build();
    }

    public static TraineeUpdatedProfileDTO mapTraineeModelToUpdatedTraineeProfileDTO(TraineeModel model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return TraineeUpdatedProfileDTO.builder()
                .userName(model.getUserName())
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .isActive(model.getIsActive())
                .dateOfBirth(model.getDateOfBirth().format(formatter))
                .address(model.getAddress())
                .build();
    }

    public static TrainerUpdatedProfileDTO mapTrainerModelToUpdatedTrainerProfileDTO(TrainerModel model) {
        return TrainerUpdatedProfileDTO.builder()
                .userName(model.getUserName())
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .specialization(model.getTrainingType().toString())
                .isActive(model.getIsActive())
                .build();
    }

    public static TrainerListItemDTO mapTrainerModelToTrainerListItemDTO(TrainerModel model) {
        return TrainerListItemDTO.builder()
                .userName(model.getUserName())
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .specialization(model.getTrainingType().toString())
                .build();
    }

    public static TraineeListItemDTO mapTraineeModelToTraineeListItemDTO(TraineeModel model) {
        return TraineeListItemDTO.builder()
                .userName(model.getUserName())
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .build();
    }

    public static void updateTraineeModelFromDTO(TraineeModel model, TraineeUpdateDTO dtoModel) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        model.setFirstName(dtoModel.getFirstName());
        model.setLastName(dtoModel.getLastName());
        if (!dtoModel.getDateOfBirth().isBlank()) {
            model.setDateOfBirth(LocalDate.parse(dtoModel.getDateOfBirth(), formatter));
        }
        if (!dtoModel.getAddress().isBlank()) {
            model.setAddress(dtoModel.getAddress());
        }
        model.setIsActive(dtoModel.isActive());
    }

    public static void updateTrainerModelFromDTO(TrainerModel model, TrainerUpdateDTO dtoModel) {
        model.setFirstName(dtoModel.getFirstName());
        model.setLastName(dtoModel.getLastName());
        model.setTrainingType(TrainingTypeEnum.valueOf(dtoModel.getSpecialization()));
        model.setIsActive(dtoModel.getIsActive());
    }

    public static TraineeTrainingListItemDTO mapTrainingModelToTraineeTrainingItem(TrainingModel trainingModel){
        return TraineeTrainingListItemDTO.builder()
                .trainingName(trainingModel.getTrainingName())
                .trainingDate(DateUtils.parseLocalDateToString(trainingModel.getTrainingDate()))
                .trainingType(trainingModel.getTrainingType().getName())
                .trainingDuration(trainingModel.getDuration())
                .build();
    }

    public static TrainerTrainingListItemDTO mapTrainingModelToTrainerTrainingItem(TrainingModel trainingModel){
        return TrainerTrainingListItemDTO.builder()
                .trainingName(trainingModel.getTrainingName())
                .trainingDate(DateUtils.parseLocalDateToString(trainingModel.getTrainingDate()))
                .trainingType(trainingModel.getTrainingType().getName())
                .trainingDuration(trainingModel.getDuration())
                .build();
    }
}

