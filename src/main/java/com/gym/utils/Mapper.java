package com.gym.utils;

import com.gym.entity.*;
import com.gym.model.*;

import java.time.ZoneId;

public class Mapper {

    public static <T extends UserModel> User mapModelToUser(T model) {
        User user = new User();
        long userId = model.getUserId();
        user.setId(userId == 0 ? null : userId);
        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        user.setUserName(model.getUserName());
        user.setPassword(model.getPassword());
        user.setIsActive(model.getIsActive());
        user.setToken(model.getToken());
        return user;
    }

    public static UserModel mapUserToUserModel(User user){
        return UserModel.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .password(user.getPassword())
                .isActive(user.getIsActive())
                .token(user.getToken())
                .build();
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
        long trainerId = trainerModel.getId();
        User user = mapModelToUser(trainerModel);
        TrainingType trainingType = mapTrainingTypeEnumToEntity(trainerModel.getTrainingType());
        Trainer trainer = new Trainer();
        trainer.setId(trainerId == 0 ? null : trainerId);
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);
        return trainer;
    }

    public static Trainee mapTraineeModelToTraineeEntity(TraineeModel traineeModel) {
        long traineeId = traineeModel.getId();
        User user = mapModelToUser(traineeModel);
        Trainee trainee = new Trainee();
        trainee.setId(traineeId == 0 ? null : traineeId);
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
