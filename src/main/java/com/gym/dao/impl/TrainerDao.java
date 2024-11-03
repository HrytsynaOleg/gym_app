package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dao.ITrainerDao;
import com.gym.entity.Trainer;
import com.gym.entity.TrainerTrainee;
import com.gym.entity.User;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.utils.JsonUtils;
import com.gym.utils.Mapper;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.stereotype.Repository;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Repository("trainerDao")
@Log4j2
public class TrainerDao implements ITrainerDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public TrainerModel create(TrainerModel trainerModel) {
        Trainer trainer = Mapper.mapTrainerModelToTrainerEntity(trainerModel);
        User user = trainer.getUser();
        try {
            entityManager.persist(user);
            trainer.setUser(user);
            entityManager.persist(trainer);
        } catch (Exception e) {
            log.error("Dao error occurred - create trainer. Transaction Id {}", MDC.get("transactionId"));
            return null;
        }
        TrainerModel newTrainerModel = get(trainer.getId());
        log.info("Trainer id = {} created in DAO. Transaction Id {}", trainer.getId(), MDC.get("transactionId"));
        return newTrainerModel;
    }

    @Override
    public TrainerModel getByUserName(String username) throws NoSuchElementException {
        String queryString = "select t from Trainer t where t.user.userName like ?1";
        TypedQuery<Trainer> query = entityManager.createQuery(queryString, Trainer.class);
        List<Trainer> resultList;
        try {
            query.setParameter(1, username);
            resultList = query.getResultList();
        } catch (Exception e) {
            log.error("Dao error occurred - get trainer by name. Transaction Id {}", MDC.get("transactionId"));
            throw new NoSuchElementException("Trainer not found");
        }
        if (resultList.size() != 1) {
            throw new NoSuchElementException("Trainer not found");
        }
        return Mapper.mapTrainerEntityToTrainerModel(resultList.get(0));
    }

    @Override
    @Transactional
    public void update(TrainerModel trainerModel) {
        Trainer trainer = Mapper.mapTrainerModelToTrainerEntity(trainerModel);
        User user = trainer.getUser();
        try {
            entityManager.merge(user);
            entityManager.merge(trainer);
        } catch (Exception e) {
            log.error("Dao error occurred - update trainer. Transaction Id {}", MDC.get("transactionId"));
        }
    }

    @Override
    public TrainerModel get(long id) throws NoSuchElementException {
        Trainer trainer;
        try {
            trainer = entityManager.find(Trainer.class, id);
        } catch (Exception e) {
            log.error("Dao error occurred - get trainer. Transaction Id {}", MDC.get("transactionId"));
            throw new NoSuchElementException("Trainer not found");
        }
        if (trainer == null) {
            throw new NoSuchElementException("Trainer not found");
        }
        return Mapper.mapTrainerEntityToTrainerModel(trainer);
    }

    @Override
    public List<TrainerModel> getNotAssignedTrainerList() {
        String queryString = "select count(u.id), t.id from Trainer t left join TrainerTrainee u " +
                "on t.id = u.trainer.id group by t.id";
        Query query = entityManager.createQuery(queryString);
        List<TrainerModel> trainerModelList;
        try {
            List resultList = query.getResultList();
            String json = JsonUtils.convertObjectToJson(resultList);
            List<List<Long>> values = JsonUtils.parseJsonString(json, new TypeReference<>() {
            });
            trainerModelList = values.stream()
                    .filter(i -> i.get(0) == 0)
                    .map(i -> i.get(1))
                    .map(i -> entityManager.find(Trainer.class, i))
                    .map(Mapper::mapTrainerEntityToTrainerModel)
                    .collect(Collectors.toList());
        }catch (Exception e){
            log.error("Dao error occurred - get not assigned trainer list. Transaction Id {}", MDC.get("transactionId"));
            return new ArrayList<>();
        }
        return trainerModelList;
    }

    @Override
    public List<TraineeModel> getAssignedTraineeList(long id) {
        String queryString = "select t from TrainerTrainee t where t.trainer.id = ?1";
        List<TraineeModel> traineeModelList;
        try {
            TypedQuery<TrainerTrainee> query = entityManager.createQuery(queryString, TrainerTrainee.class);
            query.setParameter(1, id);
            traineeModelList = query.getResultList().stream()
                    .map(TrainerTrainee::getTrainee)
                    .map(Mapper::mapTraineeEntityToTraineeModel)
                    .toList();
        }catch (Exception e) {
            log.error("Dao error occurred - get assigned trainee list. Transaction Id {}", MDC.get("transactionId"));
            return new ArrayList<>();
        }
        return traineeModelList;
    }
}
