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

@Repository("trainerJpaDao")
@Log4j2
public class TrainerJpaDao implements ITrainerDao {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public TrainerJpaDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public TrainerModel create(TrainerModel trainerModel) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Trainer trainer = Mapper.mapTrainerModelToTrainerEntity(trainerModel);
        trainer.setId(null);
        User user = trainer.getUser();
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        trainer.setUser(user);
        entityManager.persist(trainer);
        entityManager.getTransaction().commit();
        entityManager.close();
        TrainerModel newTrainerModel = Mapper.mapTrainerEntityToTrainerModel(trainer);
        log.info(String.format("Trainer id = %s created in database", trainer.getId()));
        return newTrainerModel;
    }

    @Override
    public void update(TrainerModel trainerModel) {
    }

    @Override
    public TrainerModel get(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Trainer trainer = entityManager.find(Trainer.class, id);
        entityManager.close();
        return Mapper.mapTrainerEntityToTrainerModel(trainer);
    }

    @Override
    public long getUserCountByUserName(String firstName, String lastName) {
        return 0;
    }
}
