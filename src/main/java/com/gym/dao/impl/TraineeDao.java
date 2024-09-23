package com.gym.dao.impl;

import com.gym.dao.ITraineeDao;
import com.gym.model.TraineeModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gym.utils.StorageUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.Map;

@Repository("traineeDao")
@Log4j2
public class TraineeDao implements ITraineeDao {
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public TraineeDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public TraineeModel add(TraineeModel traineeModel) {

        return null;
    }

    @Override
    public void update(TraineeModel traineeModel) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public TraineeModel getById(long id) {
        return null;
    }

    @Override
    public long getUserCountByUserName(String firstName, String lastName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select count(*) from User u where u.userName like ?1");
        query.setParameter(1, firstName + "." + lastName + "%");
        long result = (long) query.getSingleResult();
        entityManager.close();
        return result;
    }
}

