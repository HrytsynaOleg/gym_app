package com.gym.dao.impl;

import com.gym.dao.ITraineeDao;
import com.gym.dao.ITrainerDao;
import com.gym.dao.ITrainingDao;
import com.gym.entity.Trainee;
import com.gym.entity.Trainer;
import com.gym.entity.Training;
import com.gym.entity.TrainingType;
import com.gym.model.TrainingModel;
import com.gym.utils.DateUtils;
import com.gym.utils.Mapper;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("trainingDao")
@Log4j2
public class TrainingDao implements ITrainingDao {
    private final ITrainerDao trainerDao;
    private final ITraineeDao traineeDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TrainingDao(ITrainerDao trainerDao, ITraineeDao traineeDao) {
        this.trainerDao = trainerDao;
        this.traineeDao = traineeDao;
    }

    @Override
    @Transactional
    public TrainingModel create(TrainingModel trainingModel) {
        TrainingType trainingType = Mapper.mapTrainingTypeEnumToEntity(trainingModel.getTrainingType());
        Trainer trainer = Mapper.mapTrainerModelToTrainerEntity(trainerDao.get(trainingModel.getTrainerId()));
        Trainee trainee = Mapper.mapTraineeModelToTraineeEntity(traineeDao.get(trainingModel.getTraineeId()));
        Training training = new Training();
        training.setTrainingName(trainingModel.getTrainingName());
        training.setTrainingDuration(trainingModel.getDuration());
        training.setTrainingDate(DateUtils.localDateToDate(trainingModel.getTrainingDate()));
        training.setTrainingType(trainingType);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        try {
            entityManager.persist(training);
        } catch (Exception e) {
            log.error("Dao error occurred - create training Transaction Id {}", MDC.get("transactionId"));
            return null;
        }
        return get(training.getId());
    }

    @Override
    public List<TrainingModel> getTrainerTrainingListByParameters(Map<String, Object> parameters) {
        StringBuilder quertStringBuilder =
                new StringBuilder("select t from Training t where t.trainer.user.userName = :trainer");
        if (parameters.containsKey("trainee")) {
            quertStringBuilder.append(" and t.trainee.user.userName = :trainee");
        }
        if (parameters.containsKey("startDate") && parameters.containsKey("endDate")) {
            quertStringBuilder.append(" and t.trainingDate between :startDate and :endDate");
        }
        if (parameters.containsKey("trainingType")){
            quertStringBuilder.append(" and t.trainingType.id = :trainingType");
        }
        TypedQuery<Training> query = entityManager.createQuery(quertStringBuilder.toString(), Training.class);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        List<TrainingModel> resultList;
        try {
            resultList = query.getResultList().stream()
                    .map(Mapper::mapTrainingToTrainingModel)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Dao error occurred - get Trainer's training list Transaction Id {}", MDC.get("transactionId"));
            return new ArrayList<>();
        }
        return resultList;
    }

    @Override
    public List<TrainingModel> getTraineeTrainingListById(long traineeId) {
        String queryString = "select t from Training t where t.trainee.id = :trainee";
        TypedQuery<Training> query = entityManager.createQuery(queryString, Training.class);
        query.setParameter("trainee", traineeId);
        List<TrainingModel> resultList;
        try {
            resultList = query.getResultList().stream()
                    .map(Mapper::mapTrainingToTrainingModel)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Dao error occurred - get Trainee's training list Transaction Id {}",
                    MDC.get("transactionId"));
            return new ArrayList<>();
        }
        return resultList;
    }

    @Override
    public List<TrainingModel> getTraineeTrainingListByParameters(Map<String, Object> parameters) {
        StringBuilder quertStringBuilder =
                new StringBuilder("select t from Training t where t.trainee.user.userName = :trainee");
        if (parameters.containsKey("trainer")) {
            quertStringBuilder.append(" and t.trainer.user.userName like :trainer");
        }
        if (parameters.containsKey("startDate") && parameters.containsKey("endDate")) {
            quertStringBuilder.append(" and t.trainingDate between :startDate and :endDate");
        }
        if (parameters.containsKey("trainingType")){
            quertStringBuilder.append(" and t.trainingType.id = :trainingType");
        }
        TypedQuery<Training> query = entityManager.createQuery(quertStringBuilder.toString(), Training.class);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        List<TrainingModel> resultList;
        try {
            resultList = query.getResultList().stream()
                    .map(Mapper::mapTrainingToTrainingModel)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Dao error occurred - get Trainee's training list by parameters Transaction Id {}",
                    MDC.get("transactionId"));
            return new ArrayList<>();
        }
        return resultList;
    }

    @Override
    public TrainingModel get(long id) {
        TrainingModel trainingModel;
        try {
            trainingModel = Mapper.mapTrainingToTrainingModel(entityManager.find(Training.class, id));
        } catch (Exception e) {
            log.error("Dao error occurred - get training Transaction Id {}", MDC.get("transactionId"));
            return null;
        }
        return trainingModel;
    }
}
