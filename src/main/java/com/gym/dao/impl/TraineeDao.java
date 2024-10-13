package com.gym.dao.impl;

import com.gym.dao.ITraineeDao;
import com.gym.entity.Trainee;
import com.gym.entity.Trainer;
import com.gym.entity.TrainerTrainee;
import com.gym.entity.User;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.utils.Mapper;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository("traineeDao")
@Log4j2
public class TraineeDao implements ITraineeDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public TraineeModel create(TraineeModel traineeModel) {
        Trainee trainee = Mapper.mapTraineeModelToTraineeEntity(traineeModel);
        User user = trainee.getUser();
        try {
            entityManager.persist(user);
            trainee.setUser(user);
            entityManager.persist(trainee);
        } catch (Exception e) {
            log.error("Dao error occurred - create trainee. Transaction Id {}", MDC.get("transactionId"));
            return null;
        }
        TraineeModel newTraineeModel = get(trainee.getId());
        log.info("Trainee id = {} created in database. Transaction Id {}", trainee.getId(),
                MDC.get("transactionId"));
        return newTraineeModel;
    }

    @Override
    public TraineeModel getByUserName(String userName) {
        String queryString = "select t from Trainee t where t.user.userName like ?1";
        TypedQuery<Trainee> query = entityManager.createQuery(queryString, Trainee.class);
        query.setParameter(1, userName);
        List<Trainee> resultList;
        try {
            resultList = query.getResultList();
        } catch (Exception e) {
            log.error("Dao error occurred - get trainee by name. Transaction Id {}", MDC.get("transactionId"));
            return null;
        }
        if (resultList.size() != 1) {
            return null;
        }
        return Mapper.mapTraineeEntityToTraineeModel(resultList.get(0));
    }

    @Override
    @Transactional
    public void update(TraineeModel traineeModel) {
        Trainee trainee = Mapper.mapTraineeModelToTraineeEntity(traineeModel);
        User user = trainee.getUser();
        try {
            entityManager.merge(user);
            entityManager.merge(trainee);
        } catch (Exception e) {
            log.error("Dao error occurred - update trainee. Transaction Id {}", MDC.get("transactionId"));
        }
    }

    @Override
    @Transactional
    public void delete(long id) {
        try {
            Trainee trainee = entityManager.find(Trainee.class, id);
            User user = entityManager.find(User.class, trainee.getUser().getId());
            entityManager.remove(trainee);
            entityManager.remove(user);
        } catch (Exception e) {
            log.error("Dao error occurred - delete trainee. Transaction Id {}", MDC.get("transactionId"));
        }
    }

    @Override
    public TraineeModel get(long id) {
        Trainee trainee;
        try {
            trainee = entityManager.find(Trainee.class, id);
        } catch (Exception e) {
            log.error("Dao error occurred - get trainee. Transaction Id {}", MDC.get("transactionId"));
            return null;
        }
        if (trainee == null) {
            return null;
        }
        return Mapper.mapTraineeEntityToTraineeModel(trainee);
    }

    @Override
    @Transactional
    public void intendTrainer(TraineeModel traineeModel, TrainerModel trainerModel) {
        Trainer trainer = Mapper.mapTrainerModelToTrainerEntity(trainerModel);
        Trainee trainee = Mapper.mapTraineeModelToTraineeEntity(traineeModel);
        try {
            TrainerTrainee newTrainerTrainee = new TrainerTrainee();
            newTrainerTrainee.setTrainer(trainer);
            newTrainerTrainee.setTrainee(trainee);
            entityManager.persist(newTrainerTrainee);
        } catch (Exception e) {
            log.error("Dao error occurred - intend trainer. Transaction Id {}", MDC.get("transactionId"));
        }
    }

    @Override
    @Transactional
    public void deleteTrainer(TraineeModel traineeModel, TrainerModel trainerModel) {
        Trainer trainer = Mapper.mapTrainerModelToTrainerEntity(trainerModel);
        Trainee trainee = Mapper.mapTraineeModelToTraineeEntity(traineeModel);
        String queryString = "select t from TrainerTrainee t where t.trainee.id = ?1 and " +
                "t.trainer.id = ?2";
        TypedQuery<TrainerTrainee> query = entityManager.createQuery(queryString, TrainerTrainee.class);
        query.setParameter(1, trainee.getId());
        query.setParameter(2, trainer.getId());
        List<TrainerTrainee> trainerTraineeList;
        try {
            trainerTraineeList = query.getResultList();
            if (trainerTraineeList.size() == 1) {
                entityManager.remove(trainerTraineeList.get(0));
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            log.error("Dao error occurred - delete trainer. Transaction Id {}", MDC.get("transactionId"));
        }
    }

    @Override
    public List<TrainerModel> getIntendedTrainerList(TraineeModel traineeModel) {
        Trainee trainee = Mapper.mapTraineeModelToTraineeEntity(traineeModel);
        List<TrainerModel> trainerModelList;
        try {
            String queryString = "select t from TrainerTrainee t where t.trainee.id = ?1";
            TypedQuery<TrainerTrainee> query = entityManager.createQuery(queryString, TrainerTrainee.class);
            query.setParameter(1, trainee.getId());
            List<TrainerTrainee> trainerTraineeList = query.getResultList();
            trainerModelList = trainerTraineeList.stream()
                    .map(TrainerTrainee::getTrainer)
                    .map(Mapper::mapTrainerEntityToTrainerModel)
                    .toList();
        } catch (Exception e) {
            log.error("Dao error occurred - get trainer list. Transaction Id {}", MDC.get("transactionId"));
            return new ArrayList<>();
        }
        return trainerModelList;
    }
}

