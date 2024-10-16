package com.gym.dao.impl;

import com.gym.dao.IUserDao;
import com.gym.entity.User;
import com.gym.model.UserCredentials;
import com.gym.model.UserModel;
import com.gym.utils.Mapper;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.stereotype.Repository;

import jakarta.persistence.*;
import java.util.List;

@Repository("userDao")
@Log4j2
public class UserDao implements IUserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserModel getUserByName(String name) {
        String queryString = "select u from User u where u.userName like ?1";
        TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
        query.setParameter(1, name);
        List<User> resultList;
        try {
            resultList = query.getResultList();
        } catch (Exception e) {
            log.error("Dao error occurred - getUserByName. Transaction Id {}", MDC.get("transactionId"));
            return null;
        }
        if (resultList.size() != 1) {
            return null;
        }
        return Mapper.mapUserToUserModel(resultList.get(0));
    }

    @Override
    public UserCredentials getUserCredentials(String name) {
        UserModel user = getUserByName(name);
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
        Query query = entityManager.createQuery("select count(*) from User u where u.userName like ?1");
        query.setParameter(1, firstName + "." + lastName + "%");
        long result;
        try {
            result = (long) query.getSingleResult();
        }catch (Exception e) {
            log.error("Dao error occurred - getUserCount. Transaction Id {}", MDC.get("transactionId"));
            return 0;
        }
        return result;
    }

    @Override
    @Transactional
    public void update(UserModel userModel) {
        User user = Mapper.mapModelToUser(userModel);
        try {
            entityManager.merge(user);
        } catch (Exception e) {
            log.error("Dao error occurred - update user. Transaction Id {}", MDC.get("transactionId"));
        }
    }
}
