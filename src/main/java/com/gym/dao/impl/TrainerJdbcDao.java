package com.gym.dao.impl;

import com.gym.dao.ITrainerDao;
import com.gym.entity.User;
import com.gym.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

@Repository("trainerJdbcDao")
public class TrainerJdbcDao implements ITrainerDao {

    @PersistenceContext(unitName = "com.gym.entity")
    private EntityManager entityManager;

//    private final EntityManagerFactory entityManagerFactory;

//    @Autowired
//    public TrainerJdbcDao(EntityManagerFactory entityManagerFactory) {
//        this.entityManagerFactory = entityManagerFactory;
//    }

    @Override
    public Trainer create(Trainer trainer) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setUserName("userName");
        user.setPassword("123456");
        user.setIsActive(true);
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        entityManager.close();
        return null;
    }

    @Override
    public void update(Trainer trainer) {
    }

    @Override
    public Trainer get(long id) {
        return null;
    }

    @Override
    public long getUserCountByUserName(String firstName, String lastName) {
        return 0;
    }
}
