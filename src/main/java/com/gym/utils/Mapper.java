package com.gym.utils;

import com.gym.entity.*;
import com.gym.model.*;

import java.time.ZoneId;

public class Mapper {
//    public static User mapTrainerModelToUser(TrainerModel trainerModel) {
//        User user = new User();
//        user.setId(trainerModel.getUserId());
//        user.setFirstName(trainerModel.getFirstName());
//        user.setLastName(trainerModel.getLastName());
//        user.setUserName(trainerModel.getUserName());
//        user.setPassword(trainerModel.getPassword());
//        user.setIsActive(trainerModel.getIsActive());
//        return user;
//    }

    public static <T extends UserModel> User mapModelToUser(T model) {
        User user = new User();
        user.setId(model.getUserId());
        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        user.setUserName(model.getUserName());
        user.setPassword(model.getPassword());
        user.setIsActive(model.getIsActive());
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
        User user = mapModelToUser(trainerModel);
        TrainingType trainingType = mapTrainingTypeEnumToEntity(trainerModel.getTrainingType());
        Trainer trainer = new Trainer();
        trainer.setId(trainerModel.getId());
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);
        return trainer;
    }

    public static Trainee mapTraineeModelToTraineeEntity(TraineeModel traineeModel) {
        User user = mapModelToUser(traineeModel);
        Trainee trainee = new Trainee();
        trainee.setId(traineeModel.getId());
        trainee.setUser(user);
        trainee.setDateOfBirth(DateUtils.localDateToDate(traineeModel.getDateOfBirth()));
        trainee.setAddress(traineeModel.getAddress());
        return trainee;
    }

    public static TraineeModel mapTraineeEntityToTraineeModel(Trainee trainee) {
        return TraineeModel.builder()
                .id(trainee.getId())
                .userId(trainee.getUser().getId())
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .userName(trainee.getUser().getUserName())
                .password(trainee.getUser().getPassword())
                .isActive(trainee.getUser().getIsActive())
                .address(trainee.getAddress())
                .dateOfBirth(DateUtils.dateToLocalDate(trainee.getDateOfBirth()))
                .build();
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
