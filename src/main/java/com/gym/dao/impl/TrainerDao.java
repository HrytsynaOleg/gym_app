package com.gym.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gym.dao.ITrainerDao;
import com.gym.entity.Trainer;
import com.gym.entity.User;
import com.gym.model.TrainerModel;
import com.gym.utils.JsonUtils;
import com.gym.utils.Mapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository("trainerDao")
@Log4j2
public class TrainerDao implements ITrainerDao {
    private final EntityManagerFactory entityManagerFactory;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TrainerDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    @Transactional
    public TrainerModel create(TrainerModel trainerModel) {
        Trainer trainer = Mapper.mapTrainerModelToTrainerEntity(trainerModel);
        User user = trainer.getUser();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            trainer.setUser(user);
            entityManager.persist(trainer);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            log.error("Dao error occurred - create trainer ");
            return null;
        }
        TrainerModel newTrainerModel = get(trainer.getId());
        log.info(String.format("Trainer id = %s created in database", trainer.getId()));
        return newTrainerModel;
    }

    @Override
    public TrainerModel getByUserName(String username) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String queryString = "select t from Trainer t where t.user.userName like ?1";
        TypedQuery<Trainer> query = entityManager.createQuery(queryString, Trainer.class);
        List<Trainer> resultList;
        try {
            query.setParameter(1, username);
            resultList = query.getResultList();
        } catch (Exception e) {
            log.error("Dao error occurred - get trainer by name");
            return null;
        } finally {
            entityManager.close();
        }
        if (resultList.size() != 1) {
            return null;
        }
        return Mapper.mapTrainerEntityToTrainerModel(resultList.get(0));
    }

    @Override
    public void update(TrainerModel trainerModel) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Trainer trainer = Mapper.mapTrainerModelToTrainerEntity(trainerModel);
        User user = trainer.getUser();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.merge(trainer);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            log.error("Dao error occurred - update trainer");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public TrainerModel get(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Trainer trainer;
        try {
            trainer = entityManager.find(Trainer.class, id);
        } catch (Exception e) {
            log.error("Dao error occurred - get trainer");
            return null;
        } finally {
            entityManager.close();
        }
        if (trainer == null) {
            return null;
        }
        return Mapper.mapTrainerEntityToTrainerModel(trainer);
    }

    @Override
    public List<TrainerModel> getNotAssignedTrainerList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
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
            log.error("Dao error occurred - get not assigned trainer list");
            return new ArrayList<>();
        }
        finally {
            entityManager.close();
        }
        return trainerModelList;
    }
}
