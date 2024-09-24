package com.gym.dao.impl;

import com.gym.dao.ITraineeDao;
import com.gym.entity.Trainee;
import com.gym.entity.User;
import com.gym.model.TraineeModel;
import com.gym.utils.Mapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

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
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Trainee trainee = Mapper.mapTraineeModelToTraineeEntity(traineeModel);
        trainee.setId(null);
        User user = trainee.getUser();
        user.setId(null);
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        trainee.setUser(user);
        entityManager.persist(trainee);
        entityManager.getTransaction().commit();
        entityManager.close();
        TraineeModel newTraineeModel = get(trainee.getId());
        log.info(String.format("Trainee id = %s created in database", trainee.getId()));
        return newTraineeModel;
    }

    @Override
    public TraineeModel getByUserName(String userName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String queryString = "select t from Trainee t where t.user.userName like ?1";
        TypedQuery<Trainee> query = entityManager.createQuery(queryString, Trainee.class);
        query.setParameter(1, userName);
        List<Trainee> resultList = query.getResultList();
        entityManager.close();
        if (resultList.size() != 1) {
            return null;
        }
        return Mapper.mapTraineeEntityToTraineeModel(resultList.get(0));
    }

    @Override
    public void update(TraineeModel traineeModel) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Trainee trainee = Mapper.mapTraineeModelToTraineeEntity(traineeModel);
        User user = trainee.getUser();
        entityManager.getTransaction().begin();
        entityManager.merge(user);
        entityManager.merge(trainee);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void delete(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Trainee trainee = entityManager.find(Trainee.class, id);
        entityManager.remove(trainee);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public TraineeModel get(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Trainee trainee = entityManager.find(Trainee.class, id);
        entityManager.close();
        if (trainee == null) {
            return null;
        }
        return Mapper.mapTraineeEntityToTraineeModel(trainee);
    }
}

