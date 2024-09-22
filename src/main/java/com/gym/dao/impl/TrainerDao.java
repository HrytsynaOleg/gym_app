package com.gym.dao.impl;

import com.gym.dao.ITrainerDao;
import com.gym.entity.Trainer;
import com.gym.entity.User;
import com.gym.model.TrainerModel;
import com.gym.utils.Mapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

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
        String queryString = "select t from Trainer t inner join t.user u where u.userName like ?1";
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
    public long getUserCountByUserName(String firstName, String lastName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select count(*) from User u where u.userName like ?1");
        query.setParameter(1, firstName + "." + lastName);
        long result = (long) query.getSingleResult();
        entityManager.close();
        return result;
    }
}
