package com.gym;

import com.gym.config.StorageConfig;
import com.gym.dao.ITraineeDao;
import com.gym.dao.ITrainerDao;
import com.gym.dao.impl.TraineeDao;
import com.gym.dao.impl.TrainerJdbcDao;
import com.gym.model.Trainer;
import com.gym.service.ITraineeService;
import com.gym.service.impl.TraineeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GymApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(StorageConfig.class);
        ITraineeDao traineeDao = applicationContext.getBean(TraineeDao.class);
        ITrainerDao trainerJdbcDao = applicationContext.getBean(TrainerJdbcDao.class);
        ITraineeService traineeService = applicationContext.getBean(TraineeService.class);
        trainerJdbcDao.create(Trainer.builder().build());
        System.out.println();
    }
}
