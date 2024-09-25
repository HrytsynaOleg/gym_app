package com.gym.dao.impl;

import com.gym.dao.IUserDao;
import com.gym.entity.User;
import com.gym.model.UserCredentials;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository("userDao")
@Log4j2
public class UserDao implements IUserDao {
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public UserDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public User getByName(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String queryString = "select u from User u where u.userName like ?1";
        TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
        query.setParameter(1, name);
        List<User> resultList;
        try {
            resultList = query.getResultList();
        } catch (Exception e) {
            log.error("Dao error occurred - getUserByName");
            return null;
        }
        finally {
            entityManager.close();
        }
        if (resultList.size() != 1) {
            return null;
        }
        return resultList.get(0);
    }

    @Override
    public UserCredentials getUserCredentials(String name) {
        User user = getByName(name);
        if (user == null) {
            return null;
        }
        return UserCredentials.builder()
                .userName(user.getUserName())
                .password(user.getPassword())
                .build();
    }

    @Override
    public long getUserCount(String firstName, String lastName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select count(*) from User u where u.userName like ?1");
        query.setParameter(1, firstName + "." + lastName + "%");
        long result;
        try {
            result = (long) query.getSingleResult();
        }catch (Exception e) {
            log.error("Dao error occurred - getUserCount");
            return 0;
        }
        finally {
            entityManager.close();
        }
        return result;
    }
}
