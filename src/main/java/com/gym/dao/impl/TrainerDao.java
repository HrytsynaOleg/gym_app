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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@Repository("trainerDao")
@Log4j2
public class TrainerDao implements ITrainerDao {
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public TrainerDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public TrainerModel create(TrainerModel trainerModel) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Trainer trainer = Mapper.mapTrainerModelToTrainerEntity(trainerModel);
        trainer.setId(null);
        User user = trainer.getUser();
        user.setId(null);
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        trainer.setUser(user);
        entityManager.persist(trainer);
        entityManager.getTransaction().commit();
        entityManager.close();
        TrainerModel newTrainerModel = get(trainer.getId());
        log.info(String.format("Trainer id = %s created in database", trainer.getId()));
        return newTrainerModel;
    }

    @Override
    public TrainerModel getByUserName(String username) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String queryString = "select t from Trainer t where t.user.userName like ?1";
        TypedQuery<Trainer> query = entityManager.createQuery(queryString, Trainer.class);
        query.setParameter(1, username);
        List<Trainer> resultList = query.getResultList();
        entityManager.close();
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
        entityManager.getTransaction().begin();
        entityManager.merge(user);
        entityManager.merge(trainer);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public TrainerModel get(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Trainer trainer = entityManager.find(Trainer.class, id);
        entityManager.close();
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
        List resultList = query.getResultList();
        String json = JsonUtils.convertObjectToJson(resultList);
        List<List<Long>> values = JsonUtils.parseJsonString(json, new TypeReference<>() {
        });
        List<TrainerModel> trainerModelList = values.stream()
                .filter(i -> i.get(0) == 0)
                .map(i -> i.get(1))
                .map(i -> entityManager.find(Trainer.class, i))
                .map(Mapper::mapTrainerEntityToTrainerModel)
                .collect(Collectors.toList());

        entityManager.close();
        return trainerModelList;
    }
}
