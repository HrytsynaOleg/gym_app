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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("trainingDao")
@Log4j2
public class TrainingDao implements ITrainingDao {
    private final EntityManagerFactory entityManagerFactory;
    private final ITrainerDao trainerDao;
    private final ITraineeDao traineeDao;

    @Autowired
    public TrainingDao(EntityManagerFactory entityManagerFactory, ITrainerDao trainerDao, ITraineeDao traineeDao) {
        this.entityManagerFactory = entityManagerFactory;
        this.trainerDao = trainerDao;
        this.traineeDao = traineeDao;
    }

    @Override
    public TrainingModel create(TrainingModel trainingModel) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
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
            entityManager.getTransaction().begin();
            entityManager.persist(training);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            log.error("Dao error occurred - create training ");
            return null;
        } finally {
            entityManager.close();
        }
        return get(training.getId());
    }

    @Override
    public List<TrainingModel> getTrainerTrainingListByParameters(Map<String, Object> parameters) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String queryString = "select t from Training t where t.trainer.user.userName = :trainer " +
                "and t.trainingDate between :startDate and :endDate and t.trainee.user.userName = :trainee";
        TypedQuery<Training> query = entityManager.createQuery(queryString, Training.class);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        List<TrainingModel> resultList;
        try {
            resultList = query.getResultList().stream()
                    .map(Mapper::mapTrainingToTrainingModel)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Dao error occurred - get Trainer's training list");
            return new ArrayList<>();
        } finally {
            entityManager.close();
        }
        return resultList;
    }

    @Override
    public List<TrainingModel> getTraineeTrainingListById(long traineeId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String queryString = "select t from Training t where t.trainee.id = :trainee";
        TypedQuery<Training> query = entityManager.createQuery(queryString, Training.class);
        query.setParameter("trainee", traineeId);
        List<TrainingModel> resultList;
        try {
            resultList = query.getResultList().stream()
                    .map(Mapper::mapTrainingToTrainingModel)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Dao error occurred - get Trainee's training list");
            return new ArrayList<>();
        } finally {
            entityManager.close();
        }
        return resultList;
    }

    @Override
    public List<TrainingModel> getTraineeTrainingListByParameters(Map<String, Object> parameters) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String queryString = "select t from Training t where t.trainee.user.userName = :trainee " +
                "and t.trainingDate between :startDate and :endDate and t.trainer.user.userName like :trainer " +
                "and t.trainingType.id = :trainingType";
        TypedQuery<Training> query = entityManager.createQuery(queryString, Training.class);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        List<TrainingModel> resultList;
        try {
            resultList = query.getResultList().stream()
                    .map(Mapper::mapTrainingToTrainingModel)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Dao error occurred - get Trainee's training list by parameters");
            return new ArrayList<>();
        } finally {
            entityManager.close();
        }
        return resultList;
    }

    @Override
    public TrainingModel get(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TrainingModel trainingModel;
        try {
            trainingModel = Mapper.mapTrainingToTrainingModel(entityManager.find(Training.class, id));
        } catch (Exception e) {
            log.error("Dao error occurred - get training");
            return null;
        } finally {
            entityManager.close();
        }
        return trainingModel;
    }
}
