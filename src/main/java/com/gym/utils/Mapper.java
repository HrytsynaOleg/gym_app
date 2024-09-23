package com.gym.utils;

import com.gym.entity.Trainer;
import com.gym.entity.Training;
import com.gym.entity.TrainingType;
import com.gym.entity.User;
import com.gym.model.TrainerModel;
import com.gym.model.TrainingModel;
import com.gym.model.TrainingTypeEnum;

import java.time.ZoneId;

public class Mapper {
    public static User mapTrainerModelToUser(TrainerModel trainerModel) {
        User user = new User();
        user.setId(trainerModel.getUserId());
        user.setFirstName(trainerModel.getFirstName());
        user.setLastName(trainerModel.getLastName());
        user.setUserName(trainerModel.getUserName());
        user.setPassword(trainerModel.getPassword());
        user.setIsActive(trainerModel.getIsActive());
        return user;
    }

    public static TrainerModel mapTrainerEntityToTrainerModel(Trainer trainer) {
        return TrainerModel.builder()
                .id(trainer.getId())
                .userId(trainer.getUser().getId())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .userName(trainer.getUser().getUserName())
                .password(trainer.getUser().getPassword())
                .isActive(trainer.getUser().getIsActive())
                .trainingType(TrainingTypeEnum.getTrainingTypeById(trainer.getTrainingType().getId()))
                .build();
    }

    public static Trainer mapTrainerModelToTrainerEntity(TrainerModel trainerModel) {
        User user = mapTrainerModelToUser(trainerModel);
        TrainingType trainingType = mapTrainingTypeEnumToEntity(trainerModel.getTrainingType());
        Trainer trainer = new Trainer();
        trainer.setId(trainerModel.getId());
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);
        return trainer;
    }

    public static TrainingType mapTrainingTypeEnumToEntity(TrainingTypeEnum trainingTypeEnum) {
        TrainingType trainingType = new TrainingType();
        trainingType.setId(trainingTypeEnum.getId());
        trainingType.setName(trainingTypeEnum.getName());
        return trainingType;
    }

    public static TrainingTypeEnum mapTrainingTypeEntityToTrainingTypeEnum(TrainingType trainingType) {
        return TrainingTypeEnum.getTrainingTypeById(trainingType.getId());
    }

    public static TrainingModel mapTrainingToTrainingModel(Training training) {
        TrainingTypeEnum trainingTypeEnum = mapTrainingTypeEntityToTrainingTypeEnum(training.getTrainingType());
        return TrainingModel.builder()
                .id(training.getId())
                .trainerId(training.getTrainer().getId())
                .traineeId(training.getTrainee().getId())
                .trainingType(trainingTypeEnum)
                .trainingName(training.getTrainingName())
                .trainingDate(training.getTrainingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .duration(training.getTrainingDuration())
                .build();
    }
}
