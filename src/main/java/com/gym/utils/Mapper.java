package com.gym.utils;

import com.gym.entity.Trainer;
import com.gym.entity.User;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingType;

public class Mapper {
    public static User mapTrainerModelToUser(TrainerModel trainerModel){
        return User.builder()
                .firstName(trainerModel.getFirstName())
                .lastName(trainerModel.getLastName())
                .userName(trainerModel.getUserName())
                .password(trainerModel.getPassword())
                .isActive(trainerModel.getIsActive())
                .build();
    }

    public static TrainerModel mapTrainerEntityToTrainerModel(Trainer trainer){
        return TrainerModel.builder()
                .id(trainer.getId())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .userName(trainer.getUser().getUserName())
                .password(trainer.getUser().getPassword())
                .isActive(trainer.getUser().getIsActive())
                .trainingType(TrainingType.getTrainingTypeById(trainer.getTrainingTypeId()))
                .build();
    }

    public static Trainer mapTrainerModelToTrainerEntity(TrainerModel trainerModel){
        User user = mapTrainerModelToUser(trainerModel);
        return Trainer.builder()
                .id(trainerModel.getId())
                .trainingTypeId(trainerModel.getTrainingType().getId())
                .user(user)
                .build();
    }
}
