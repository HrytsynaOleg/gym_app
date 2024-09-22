package com.gym;

import com.gym.config.StorageConfig;
import com.gym.dao.ITraineeDao;
import com.gym.dao.impl.TraineeDao;
import com.gym.service.ITraineeService;
import com.gym.service.ITrainerService;
import com.gym.service.impl.TraineeService;
import com.gym.service.impl.TrainerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GymApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(StorageConfig.class);
        ITraineeDao traineeDao = applicationContext.getBean(TraineeDao.class);
        ITraineeService traineeService = applicationContext.getBean(TraineeService.class);
        ITrainerService trainerService = applicationContext.getBean(TrainerService.class);
        System.out.println();
    }
}
